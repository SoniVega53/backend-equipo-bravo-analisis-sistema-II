package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.ControlStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.dto.liquidacion.EmpleadoBaseDto;
import backend_equipo_bravo.analisis_sistema_II.dto.liquidacion.LiquidacionDto;
import backend_equipo_bravo.analisis_sistema_II.entity.Empleado;
import backend_equipo_bravo.analisis_sistema_II.entity.Liquidacion;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LiquidacionService extends BaseService<Liquidacion, Integer> {

    @Autowired
    private LiquidacionRepository liquidacionRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PuestoRepository puestoRepository;
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private StatusEmpleadoRepository statusEmpleadoRepository;
    @Autowired
    private FlujoStatusEmpleadoRepository flujoStatusEmpleadoRepository;

    @Override
    protected JpaRepository<Liquidacion, Integer> getRepository() {
        return liquidacionRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    public EmpleadoBaseDto obtenerEmpleadoBase(Integer idEmpleado) {
        validarConsulta();

        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));

        return mapearAEmpleadoBaseDto(empleado);
    }

    public List<EmpleadoBaseDto> listarEmpleadosBase() {
        validarConsulta();

        return empleadoRepository.findAll().stream()
                .map(this::mapearAEmpleadoBaseDto)
                .collect(Collectors.toList());
    }

    public LiquidacionDto obtenerPorEmpleado(Integer idEmpleado) {
        validarConsulta();

        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));

        Optional<Liquidacion> liquidacionOpt = liquidacionRepository
                .findByIdEmpleadoAndFechaContratacion(idEmpleado, empleado.getFechaContratacion());

        if (liquidacionOpt.isPresent()) {
            return mapearADto(liquidacionOpt.get());
        }

        LiquidacionDto dto = new LiquidacionDto();
        dto.setIdEmpleado(empleado.getIdEmpleado());
        dto.setIdStatusEmpleado(empleado.getIdStatusEmpleado());
        if (empleado.getIdStatusEmpleado() != null) {
            statusEmpleadoRepository.findById(empleado.getIdStatusEmpleado()).ifPresent(status ->
                    dto.setNombreStatus(status.getNombre())
            );
        }
        dto.setIdPuesto(empleado.getIdPuesto());
        dto.setFechaContratacion(empleado.getFechaContratacion());
        dto.setIngresoSueldoBase(empleado.getIngresoSueldoBase());
        dto.setIngresoBonificacionDecreto(empleado.getIngresoBonificacionDecreto());
        dto.setIngresoOtrosIngresos(empleado.getIngresoOtrosIngresos());
        dto.setDescuentoIgss(empleado.getDescuentoIgss());
        dto.setDescuentoIsr(empleado.getDescuentoIsr());
        dto.setDescuentoInasistencias(empleado.getDescuentoInasistencias());

        personaRepository.findById(empleado.getIdPersona()).ifPresent(persona ->
                dto.setNombreEmpleado(persona.getNombre() + " " + persona.getApellido())
        );

        puestoRepository.findById(empleado.getIdPuesto()).ifPresent(puesto -> {
            dto.setNombrePuesto(puesto.getNombre());
            dto.setIdDepartamento(puesto.getIdDepartamento());
            if (puesto.getIdDepartamento() != null) {
                departamentoRepository.findById(puesto.getIdDepartamento()).ifPresent(dep ->
                        dto.setNombreDepartamento(dep.getNombre())
                );
            }
        });

        return dto;
    }

    public List<LiquidacionDto> obtenerHistorialPorEmpleado(Integer idEmpleado) {
        validarConsulta();
        return liquidacionRepository.findByIdEmpleado(idEmpleado).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Transactional
    public LiquidacionDto procesarLiquidacion(LiquidacionDto request) {

        if (request.getIdEmpleado() == null) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }

        boolean esNuevo = request.getIdLiquidacion() == null;

        if (esNuevo) {
            validarAlta();
        } else {
            validarCambio();
        }

        Empleado empleado = empleadoRepository.findById(request.getIdEmpleado())
                .orElseThrow(() ->
                        new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));

        Liquidacion liquidacion;

        if (esNuevo) {
            liquidacion = new Liquidacion();
            liquidacion.setFechaCreacion(LocalDateTime.now());
            liquidacion.setUsuarioCreacion(obtenerUsuarioAutenticado());
        } else {
            liquidacion = liquidacionRepository.findById(request.getIdLiquidacion())
                    .orElseThrow(() ->
                            new BusinessException(GeneralError.ERROR_SERVICE));
        }

        LocalDate fechaContratacion = request.getFechaContratacion() != null ? request.getFechaContratacion() : empleado.getFechaContratacion();
        LocalDate fechaLiquidacion = request.getFechaLiquidacion() != null ? request.getFechaLiquidacion() : LocalDate.now();

        if (fechaContratacion != null && request.getFechaEgreso() != null) {
            if (request.getFechaEgreso().isBefore(fechaContratacion)) {
                throw new BusinessException(GeneralError.RANGO_FECHAS_INVALIDO);
            }
        }

        liquidacion.setIdEmpleado(empleado.getIdEmpleado());
        liquidacion.setFechaContratacion(fechaContratacion);
        liquidacion.setFechaEgreso(request.getFechaEgreso());
        liquidacion.setFechaLiquidacion(fechaLiquidacion);
        liquidacion.setMotivoEgreso(request.getMotivoEgreso());

        // Puesto seleccionado o por defecto el del empleado
        Integer idPuesto = request.getIdPuesto() != null ? request.getIdPuesto() : empleado.getIdPuesto();
        liquidacion.setIdPuesto(idPuesto);

        BigDecimal sueldoBase = valorOZero(request.getIngresoSueldoBase());
        BigDecimal bonificacion = valorOZero(request.getIngresoBonificacionDecreto());
        BigDecimal otrosIngresos = valorOZero(request.getIngresoOtrosIngresos());

        BigDecimal descuentoIgss = valorOZero(request.getDescuentoIgss());
        BigDecimal descuentoIsr = valorOZero(request.getDescuentoIsr());
        BigDecimal descuentoInasistencias = valorOZero(request.getDescuentoInasistencias());

        liquidacion.setIngresoSueldoBase(sueldoBase);
        liquidacion.setIngresoBonificacionDecreto(bonificacion);
        liquidacion.setIngresoOtrosIngresos(otrosIngresos);

        liquidacion.setDescuentoIgss(descuentoIgss);
        liquidacion.setDescuentoIsr(descuentoIsr);
        liquidacion.setDescuentoInasistencias(descuentoInasistencias);

        // CÁLCULOS
        BigDecimal totalIngresos = sueldoBase
                .add(bonificacion)
                .add(otrosIngresos);

        BigDecimal totalDescuentos = descuentoIgss
                .add(descuentoIsr)
                .add(descuentoInasistencias);

        BigDecimal salarioNeto = totalIngresos
                .subtract(totalDescuentos);

        liquidacion.setTotalIngresos(totalIngresos);
        liquidacion.setTotalDescuentos(totalDescuentos);
        liquidacion.setSalarioNeto(salarioNeto);

        liquidacion.setTotalNeto(salarioNeto);

        // ACTUALIZACIÓN DE ESTATUS DEL EMPLEADO (Baja / Despedido)

        Integer nuevoStatus = request.getIdStatusEmpleado() != null
                ? request.getIdStatusEmpleado()
                : ControlStatusEmpleado.BAJA.getId();

       // Integer nuevoStatus = ControlStatusEmpleado.BAJA.getId();

        Integer actualStatus = empleado.getIdStatusEmpleado();
        if (actualStatus != null && !actualStatus.equals(nuevoStatus)) {
            boolean transicionValida = flujoStatusEmpleadoRepository.existsById(
                    new FlujoStatusEmpleadoId(actualStatus, nuevoStatus)
            );
            if (!transicionValida && flujoStatusEmpleadoRepository.count() > 0) {
                throw new BusinessException(GeneralError.TRANSICION_STATUS_INVALIDA);
            }
            empleado.setIdStatusEmpleado(nuevoStatus);
            empleado.setFechaModificacion(LocalDateTime.now());
            empleado.setUsuarioModificacion(obtenerUsuarioAutenticado());
            empleadoRepository.save(empleado);
        }

        return mapearADto(crearBase(liquidacion));
    }

    private BigDecimal valorOZero(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    public List<LiquidacionDto> obtenerTodas() {
        validarConsulta();
        return buscarTodos().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    public LiquidacionDto obtenerPorIdLiquidacion(Integer id) {
        validarConsulta();
        return mapearADto(buscarPorId(id));
    }

    private LiquidacionDto mapearADto(Liquidacion entidad) {
        LiquidacionDto dto = new LiquidacionDto();
        dto.setIdLiquidacion(entidad.getIdLiquidacion());
        dto.setIdEmpleado(entidad.getIdEmpleado());
        dto.setFechaContratacion(entidad.getFechaContratacion());
        dto.setFechaEgreso(entidad.getFechaEgreso());
        dto.setFechaLiquidacion(entidad.getFechaLiquidacion());
        dto.setMotivoEgreso(entidad.getMotivoEgreso());
        dto.setIdPuesto(entidad.getIdPuesto());
        dto.setIngresoSueldoBase(entidad.getIngresoSueldoBase());
        dto.setIngresoBonificacionDecreto(entidad.getIngresoBonificacionDecreto());
        dto.setIngresoOtrosIngresos(entidad.getIngresoOtrosIngresos());
        dto.setDescuentoIgss(entidad.getDescuentoIgss());
        dto.setDescuentoIsr(entidad.getDescuentoIsr());
        dto.setDescuentoInasistencias(entidad.getDescuentoInasistencias());
        dto.setSalarioNeto(entidad.getSalarioNeto());
        dto.setTotalIngresos(entidad.getTotalIngresos());
        dto.setTotalDescuentos(entidad.getTotalDescuentos());
        dto.setTotalNeto(entidad.getTotalNeto());
        dto.setFechaCreacion(entidad.getFechaCreacion());

        empleadoRepository.findById(entidad.getIdEmpleado()).ifPresent(empleado -> {
            dto.setIdStatusEmpleado(empleado.getIdStatusEmpleado());
            if (empleado.getIdStatusEmpleado() != null) {
                statusEmpleadoRepository.findById(empleado.getIdStatusEmpleado()).ifPresent(status ->
                        dto.setNombreStatus(status.getNombre())
                );
            }
            personaRepository.findById(empleado.getIdPersona()).ifPresent(persona ->
                    dto.setNombreEmpleado(persona.getNombre() + " " + persona.getApellido())
            );
        });

        if (entidad.getIdPuesto() != null) {
            puestoRepository.findById(entidad.getIdPuesto()).ifPresent(puesto -> {
                dto.setNombrePuesto(puesto.getNombre());
                dto.setIdDepartamento(puesto.getIdDepartamento());
                if (puesto.getIdDepartamento() != null) {
                    departamentoRepository.findById(puesto.getIdDepartamento()).ifPresent(dep ->
                            dto.setNombreDepartamento(dep.getNombre())
                    );
                }
            });
        }

        return dto;
    }

    private EmpleadoBaseDto mapearAEmpleadoBaseDto(Empleado empleado) {
        EmpleadoBaseDto dto = new EmpleadoBaseDto();
        dto.setIdEmpleado(empleado.getIdEmpleado());
        dto.setIdPersona(empleado.getIdPersona());
        dto.setIdSucursal(empleado.getIdSucursal());
        dto.setIdPuesto(empleado.getIdPuesto());
        dto.setIdStatusEmpleado(empleado.getIdStatusEmpleado());
        dto.setFechaContratacion(empleado.getFechaContratacion());
        dto.setIngresoSueldoBase(empleado.getIngresoSueldoBase());
        dto.setIngresoBonificacionDecreto(empleado.getIngresoBonificacionDecreto());
        dto.setIngresoOtrosIngresos(empleado.getIngresoOtrosIngresos());
        dto.setDescuentoIgss(empleado.getDescuentoIgss());
        dto.setDescuentoIsr(empleado.getDescuentoIsr());
        dto.setDescuentoInasistencias(empleado.getDescuentoInasistencias());

        if (empleado.getIdPersona() != null) {
            personaRepository.findById(empleado.getIdPersona()).ifPresent(persona ->
                    dto.setNombreEmpleado(persona.getNombre() + " " + persona.getApellido())
            );
        }

        if (empleado.getIdPuesto() != null) {
            puestoRepository.findById(empleado.getIdPuesto()).ifPresent(puesto -> {
                dto.setNombrePuesto(puesto.getNombre());
                dto.setIdDepartamento(puesto.getIdDepartamento());
                if (puesto.getIdDepartamento() != null) {
                    departamentoRepository.findById(puesto.getIdDepartamento()).ifPresent(dep ->
                            dto.setNombreDepartamento(dep.getNombre())
                    );
                }
            });
        }

        if (empleado.getIdStatusEmpleado() != null) {
            statusEmpleadoRepository.findById(empleado.getIdStatusEmpleado()).ifPresent(status ->
                    dto.setNombreStatus(status.getNombre())
            );
        }

        return dto;
    }
}