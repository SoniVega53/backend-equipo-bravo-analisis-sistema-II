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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
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
                .orElseThrow(() -> new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));

        Liquidacion liquidacion;
        if (esNuevo) {
            liquidacion = new Liquidacion();
            liquidacion.setFechaCreacion(LocalDateTime.now());
            liquidacion.setUsuarioCreacion(obtenerUsuarioAutenticado());
        } else {
            liquidacion = liquidacionRepository.findById(request.getIdLiquidacion())
                    .orElseThrow(() -> new BusinessException(GeneralError.ERROR_SERVICE));
        }

        LocalDate fechaContratacion = request.getFechaContratacion() != null ? request.getFechaContratacion() : empleado.getFechaContratacion();
        LocalDate fechaEgreso = request.getFechaEgreso() != null ? request.getFechaEgreso() : LocalDate.now();

        if (fechaContratacion != null && fechaEgreso != null) {
            if (fechaEgreso.isBefore(fechaContratacion)) {
                throw new BusinessException(GeneralError.RANGO_FECHAS_INVALIDO);
            }
        }

        Integer nuevoStatus = request.getIdStatusEmpleado() != null ? request.getIdStatusEmpleado() : ControlStatusEmpleado.BAJA.getId();
        boolean esDespido = nuevoStatus == 5;
        String motivoEgreso = esDespido ? "Despido" : "Renuncia";

        liquidacion.setIdEmpleado(empleado.getIdEmpleado());
        liquidacion.setFechaContratacion(fechaContratacion);
        liquidacion.setFechaEgreso(fechaEgreso);
        liquidacion.setFechaLiquidacion(LocalDate.now());
        liquidacion.setMotivoEgreso(motivoEgreso);

        Integer idPuesto = request.getIdPuesto() != null ? request.getIdPuesto() : empleado.getIdPuesto();
        liquidacion.setIdPuesto(idPuesto);

        BigDecimal salarioBase = valorOZero(request.getIngresoSueldoBase() != null ? request.getIngresoSueldoBase() : empleado.getIngresoSueldoBase());
        
        // --- REALIZAR LOS CÁLCULOS DINÁMICOS AL ESTILO GUATEMALA ---
        LiquidacionDto calculosTemporales = calcularDesglose(salarioBase, fechaContratacion, fechaEgreso, esDespido);
        
        // Guardar las agrupaciones en los campos existentes de la base de datos
        liquidacion.setIngresoSueldoBase(calculosTemporales.getMontoSalarioPendiente());
        liquidacion.setIngresoBonificacionDecreto(calculosTemporales.getMontoBonificacionDecretoPendiente());
        
        // Se agrupan todas las prestaciones e indemnización en "ingresoOtrosIngresos" para no afectar la DB
        BigDecimal otrosExtras = valorOZero(request.getIngresoOtrosIngresos());
        BigDecimal totalPrestaciones = calculosTemporales.getMontoIndemnizacion()
                .add(calculosTemporales.getMontoAguinaldo())
                .add(calculosTemporales.getMontoBono14())
                .add(calculosTemporales.getMontoVacaciones())
                .add(otrosExtras);
        liquidacion.setIngresoOtrosIngresos(totalPrestaciones);

        // Descuentos: se aplica el IGSS solo al salario ordinario (calculado previamente)
        liquidacion.setDescuentoIgss(calculosTemporales.getDescuentoIgss());
        liquidacion.setDescuentoIsr(valorOZero(request.getDescuentoIsr()));
        liquidacion.setDescuentoInasistencias(valorOZero(request.getDescuentoInasistencias()));

        BigDecimal totalIngresos = liquidacion.getIngresoSueldoBase()
                .add(liquidacion.getIngresoBonificacionDecreto())
                .add(liquidacion.getIngresoOtrosIngresos());

        BigDecimal totalDescuentos = liquidacion.getDescuentoIgss()
                .add(liquidacion.getDescuentoIsr())
                .add(liquidacion.getDescuentoInasistencias());

        BigDecimal salarioNeto = totalIngresos.subtract(totalDescuentos);

        liquidacion.setTotalIngresos(totalIngresos);
        liquidacion.setTotalDescuentos(totalDescuentos);
        liquidacion.setSalarioNeto(salarioNeto);
        liquidacion.setTotalNeto(salarioNeto);

        // Actualizar Status
        Integer actualStatus = empleado.getIdStatusEmpleado();
        if (actualStatus != null && !actualStatus.equals(nuevoStatus)) {
            boolean transicionValida = flujoStatusEmpleadoRepository.existsById(new FlujoStatusEmpleadoId(actualStatus, nuevoStatus));
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

    private LiquidacionDto calcularDesglose(BigDecimal salarioBase, LocalDate contratacion, LocalDate egreso, boolean esDespido) {
        LiquidacionDto dto = new LiquidacionDto();
        
        int diasTotales = (int) ChronoUnit.DAYS.between(contratacion, egreso);
        if (diasTotales < 0) diasTotales = 0;
        dto.setDiasLaboradosTotal(diasTotales);

        // Indemnización
        BigDecimal salarioPromedioIndemnizacion = salarioBase.multiply(new BigDecimal("14")).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal montoIndemnizacion = BigDecimal.ZERO;
        if (esDespido) {
            montoIndemnizacion = salarioPromedioIndemnizacion.divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasTotales)).setScale(2, RoundingMode.HALF_UP);
        }
        dto.setMontoIndemnizacion(montoIndemnizacion);

        // Aguinaldo
        LocalDate inicioAguinaldo = egreso.getMonthValue() == 12 ? LocalDate.of(egreso.getYear(), 12, 1) : LocalDate.of(egreso.getYear() - 1, 12, 1);
        if (inicioAguinaldo.isBefore(contratacion)) inicioAguinaldo = contratacion;
        int diasAguinaldo = (int) ChronoUnit.DAYS.between(inicioAguinaldo, egreso);
        if(diasAguinaldo < 0) diasAguinaldo = 0;
        BigDecimal montoAguinaldo = salarioBase.divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasAguinaldo)).setScale(2, RoundingMode.HALF_UP);
        dto.setDiasProporcionalesAguinaldo(diasAguinaldo);
        dto.setMontoAguinaldo(montoAguinaldo);

        // Bono 14
        LocalDate inicioBono14 = egreso.getMonthValue() >= 7 ? LocalDate.of(egreso.getYear(), 7, 1) : LocalDate.of(egreso.getYear() - 1, 7, 1);
        if (inicioBono14.isBefore(contratacion)) inicioBono14 = contratacion;
        int diasBono14 = (int) ChronoUnit.DAYS.between(inicioBono14, egreso);
        if(diasBono14 < 0) diasBono14 = 0;
        BigDecimal montoBono14 = salarioBase.divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasBono14)).setScale(2, RoundingMode.HALF_UP);
        dto.setDiasProporcionalesBono14(diasBono14);
        dto.setMontoBono14(montoBono14);

        // Vacaciones
        int aniosCompletos = Period.between(contratacion, egreso).getYears();
        LocalDate ultimoAniversario = contratacion.plusYears(aniosCompletos);
        int diasVacaciones = (int) ChronoUnit.DAYS.between(ultimoAniversario, egreso);
        if(diasVacaciones < 0) diasVacaciones = 0;
        BigDecimal montoVacaciones = salarioBase.divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("15")).multiply(new BigDecimal(diasVacaciones)).divide(new BigDecimal("365"), 2, RoundingMode.HALF_UP);
        dto.setDiasProporcionalesVacaciones(diasVacaciones);
        dto.setMontoVacaciones(montoVacaciones);

        // Salario Pendiente
        int diasPendientes = egreso.getDayOfMonth();
        BigDecimal montoSalarioPendiente = salarioBase.divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasPendientes)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal montoBonoDecreto = new BigDecimal("250").divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasPendientes)).setScale(2, RoundingMode.HALF_UP);

        dto.setDiasPendientesPago(diasPendientes);
        dto.setMontoSalarioPendiente(montoSalarioPendiente);
        dto.setMontoBonificacionDecretoPendiente(montoBonoDecreto);

        // Descuentos
        BigDecimal descuentoIgss = montoSalarioPendiente.multiply(new BigDecimal("0.0483")).setScale(2, RoundingMode.HALF_UP);
        dto.setDescuentoIgss(descuentoIgss);

        return dto;
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

        // Campos de DB
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

        // Reconstruimos el desglose en base al salario base original
        BigDecimal salarioOriginal = BigDecimal.ZERO;
        boolean esDespido = "Despido".equalsIgnoreCase(entidad.getMotivoEgreso());
        
        Optional<Empleado> empOpt = empleadoRepository.findById(entidad.getIdEmpleado());
        if (empOpt.isPresent()) {
            salarioOriginal = empOpt.get().getIngresoSueldoBase();
        }

        // Si tenemos datos, re-calculamos el desglose
        if (entidad.getFechaContratacion() != null && entidad.getFechaEgreso() != null && salarioOriginal != null && salarioOriginal.compareTo(BigDecimal.ZERO) > 0) {
            LiquidacionDto desglose = calcularDesglose(salarioOriginal, entidad.getFechaContratacion(), entidad.getFechaEgreso(), esDespido);
            
            dto.setDiasLaboradosTotal(desglose.getDiasLaboradosTotal());
            dto.setMontoIndemnizacion(desglose.getMontoIndemnizacion());
            dto.setDiasProporcionalesAguinaldo(desglose.getDiasProporcionalesAguinaldo());
            dto.setMontoAguinaldo(desglose.getMontoAguinaldo());
            dto.setDiasProporcionalesBono14(desglose.getDiasProporcionalesBono14());
            dto.setMontoBono14(desglose.getMontoBono14());
            dto.setDiasProporcionalesVacaciones(desglose.getDiasProporcionalesVacaciones());
            dto.setMontoVacaciones(desglose.getMontoVacaciones());
            dto.setDiasPendientesPago(desglose.getDiasPendientesPago());
            dto.setMontoSalarioPendiente(desglose.getMontoSalarioPendiente());

            // Deduce "Otros Ingresos" reales
            BigDecimal sumPrestaciones = desglose.getMontoIndemnizacion()
                .add(desglose.getMontoAguinaldo())
                .add(desglose.getMontoBono14())
                .add(desglose.getMontoVacaciones());
            
            BigDecimal purosOtros = valorOZero(entidad.getIngresoOtrosIngresos()).subtract(sumPrestaciones);
            // Si por error de rounding da un negativo tiny, lo ponemos en 0
            if(purosOtros.compareTo(BigDecimal.ZERO) < 0) purosOtros = BigDecimal.ZERO;
            
            dto.setIngresoOtrosIngresos(purosOtros);
        }

        if (empOpt.isPresent()) {
            Empleado empleado = empOpt.get();
            dto.setIdStatusEmpleado(empleado.getIdStatusEmpleado());
            if (empleado.getIdStatusEmpleado() != null) {
                statusEmpleadoRepository.findById(empleado.getIdStatusEmpleado()).ifPresent(status ->
                        dto.setNombreStatus(status.getNombre())
                );
            }
            personaRepository.findById(empleado.getIdPersona()).ifPresent(persona ->
                    dto.setNombreEmpleado(persona.getNombre() + " " + persona.getApellido())
            );
        }

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