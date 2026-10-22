package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.ControlStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.dto.EmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.dto.SelectOptionDto;
import backend_equipo_bravo.analisis_sistema_II.entity.Empleado;
import backend_equipo_bravo.analisis_sistema_II.entity.StatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.EmpleadoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.FlujoStatusEmpleadoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.PersonaRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.PuestoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.StatusEmpleadoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmpleadoService extends BaseService<Empleado, Integer> {

    private static final BigDecimal MAXIMO_MONTO = new BigDecimal("99999999.99");

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private StatusEmpleadoRepository statusEmpleadoRepository;

    @Autowired
    private FlujoStatusEmpleadoRepository flujoStatusEmpleadoRepository;

    @Override
    protected JpaRepository<Empleado, Integer> getRepository() {
        return empleadoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND);
    }

    public Empleado crear(EmpleadoRequest request) {
        validarDatos(request);

        // Un empleado nuevo siempre inicia como Activo
        if (request.getIdStatusEmpleado() != ControlStatusEmpleado.ACTIVO.getId()) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "Un empleado nuevo debe crearse con estatus Activo.");
        }
        validarPersonaDisponible(request.getIdPersona(), null);

        String usuarioActual = obtenerUsuarioAutenticado();

        Empleado empleado = new Empleado();
        mapearDatosRequest(empleado, request);

        empleado.setUsuarioCreacion(usuarioActual);
        empleado.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(empleado);
    }

    public Empleado actualizar(Integer id, EmpleadoRequest request) {
        validarDatos(request);
        Empleado existente = super.buscarPorId(id);

        validarPersonaDisponible(request.getIdPersona(), id);
        validarCambioStatus(existente.getIdStatusEmpleado(), request.getIdStatusEmpleado());

        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    /**
     * Estatus que puede tomar un empleado: el actual más los destinos permitidos
     * por la tabla FLUJO_STATUS_EMPLEADO.
     */
    public List<SelectOptionDto> statusPermitidos(Integer idEmpleado) {
        validarConsulta();
        Empleado empleado = super.buscarPorId(idEmpleado);
        Integer actual = empleado.getIdStatusEmpleado();

        List<Integer> ids = new ArrayList<>();
        ids.add(actual);
        flujoStatusEmpleadoRepository.findAll().stream()
                .filter(f -> f.getIdStatusActual().equals(actual))
                .forEach(f -> ids.add(f.getIdStatusNuevo()));

        List<SelectOptionDto> opciones = new ArrayList<>();
        for (Integer idStatus : ids) {
            StatusEmpleado status = statusEmpleadoRepository.findById(idStatus).orElse(null);
            if (status != null) {
                opciones.add(new SelectOptionDto(status.getIdStatusEmpleado(), status.getNombre()));
            }
        }
        return opciones;
    }

    private void validarCambioStatus(Integer actual, Integer nuevo) {
        if (actual == null || actual.equals(nuevo)) {
            return;
        }
        boolean transicionValida = flujoStatusEmpleadoRepository.existsById(new FlujoStatusEmpleadoId(actual, nuevo));
        if (!transicionValida && flujoStatusEmpleadoRepository.count() > 0) {
            throw new BusinessException(GeneralError.TRANSICION_STATUS_INVALIDA);
        }
    }

    private void validarPersonaDisponible(Integer idPersona, Integer idEmpleadoActual) {
        Empleado filtro = new Empleado();
        filtro.setIdPersona(idPersona);
        boolean ocupada = empleadoRepository.findAll(Example.of(filtro)).stream()
                .anyMatch(e -> idEmpleadoActual == null || !e.getIdEmpleado().equals(idEmpleadoActual));
        if (ocupada) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "La persona seleccionada ya está registrada como empleado.");
        }
    }

    private void validarDatos(EmpleadoRequest r) {
        if (r.getIdPersona() == null || !personaRepository.existsById(r.getIdPersona())) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "La persona seleccionada no existe.");
        }
        if (r.getIdSucursal() == null || !sucursalRepository.existsById(r.getIdSucursal())) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "La sucursal seleccionada no existe.");
        }
        if (r.getIdPuesto() == null || !puestoRepository.existsById(r.getIdPuesto())) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "El puesto seleccionado no existe.");
        }
        if (r.getIdStatusEmpleado() == null || !statusEmpleadoRepository.existsById(r.getIdStatusEmpleado())) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "El estatus seleccionado no existe.");
        }
        if (r.getFechaContratacion() == null) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "La fecha de contratación es obligatoria.");
        }
        if (r.getFechaContratacion().isAfter(LocalDate.now())) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "La fecha de contratación no puede ser futura.");
        }

        monto(r.getIngresoSueldoBase(), "El sueldo base", true);
        monto(r.getIngresoBonificacionDecreto(), "La bonificación decreto", false);
        monto(r.getIngresoOtrosIngresos(), "Otros ingresos", false);
        monto(r.getDescuentoIgss(), "El descuento IGSS", false);
        monto(r.getDescuentoIsr(), "El descuento ISR", false);
        monto(r.getDescuentoInasistencias(), "El descuento por inasistencias", false);
    }

    private void monto(BigDecimal valor, String campo, boolean mayorACero) {
        if (valor == null) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, campo + " es obligatorio.");
        }
        if (mayorACero ? valor.signum() <= 0 : valor.signum() < 0) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS,
                    campo + (mayorACero ? " debe ser mayor a cero." : " no puede ser negativo."));
        }
        if (valor.compareTo(MAXIMO_MONTO) > 0) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, campo + " excede el monto máximo permitido.");
        }
    }

    private void mapearDatosRequest(Empleado e, EmpleadoRequest r) {
        e.setIdPersona(r.getIdPersona());
        e.setIdSucursal(r.getIdSucursal());
        e.setFechaContratacion(r.getFechaContratacion());
        e.setIdPuesto(r.getIdPuesto());
        e.setIdStatusEmpleado(r.getIdStatusEmpleado());
        e.setIngresoSueldoBase(redondear(r.getIngresoSueldoBase()));
        e.setIngresoBonificacionDecreto(redondear(r.getIngresoBonificacionDecreto()));
        e.setIngresoOtrosIngresos(redondear(r.getIngresoOtrosIngresos()));
        e.setDescuentoIgss(redondear(r.getDescuentoIgss()));
        e.setDescuentoIsr(redondear(r.getDescuentoIsr()));
        e.setDescuentoInasistencias(redondear(r.getDescuentoInasistencias()));
    }

    private BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
