package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.ControlStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaDetalleDto;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaRequestDto;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaResponseDto;
import backend_equipo_bravo.analisis_sistema_II.entity.*;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalculoPlanillaService extends BaseService<PlanillaCabecera, Object> {

    @Autowired
    private PlanillaCabeceraRepository planillaCabeceraRepository;
    @Autowired
    private PlanillaDetalleRepository planillaDetalleRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private InasistenciaRepository inasistenciaRepository;
    @Autowired
    private PeriodoPlanillaRepository periodoPlanillaRepository;
    @Autowired
    private PersonaRepository personaRepository;

    @Override
    @SuppressWarnings("unchecked")
    protected JpaRepository<PlanillaCabecera, Object> getRepository() {
        return (JpaRepository<PlanillaCabecera, Object>) (JpaRepository<?, ?>) planillaCabeceraRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Object id) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    public CalculoPlanillaService() {
        setEntityName("CalculoPlanilla");
    }

    @Transactional
    public PlanillaResponseDto procesarOObtenerPlanilla(PlanillaRequestDto request) {
        if (request.getIsUpdate())
            validarCambio();
        else if (request.getIsFind())
            validarConsulta();
        else
            validarAlta();

        if (request.getForzarRecalculo()) {
            planillaDetalleRepository.deleteByAnioAndMes(request.getAnio(), request.getMes());
            planillaCabeceraRepository.deleteByAnioAndMes(request.getAnio(), request.getMes());
        } else {
            PlanillaCabecera cabeceraExistente = planillaCabeceraRepository.findByAnioAndMes(request.getAnio(), request.getMes())
                    .orElseThrow(() -> new BusinessException(GeneralError.PLANILLA_NOT_FOUND));

            if (cabeceraExistente != null) {
                return construirRespuestaDesdeBase(cabeceraExistente);
            }
        }

        return generarNuevaPlanilla(request.getForzarRecalculo(), request.getAnio(), request.getMes());
    }

    private PlanillaResponseDto generarNuevaPlanilla(boolean forzar, Integer anio, Integer mes) {
        Optional<PeriodoPlanilla> periodoOp = periodoPlanillaRepository.findByAnioAndMes(anio, mes);
        PeriodoPlanilla periodo = periodoOp.get();

        if (periodo == null) {
            periodo = new PeriodoPlanilla();
            periodo.setAnio(anio);
            periodo.setMes(mes);
            periodo.setFechaCreacion(LocalDateTime.now());
            periodo.setUsuarioCreacion(obtenerUsuarioAutenticado());
        }


        PlanillaCabecera cabecera = new PlanillaCabecera();
        cabecera.setAnio(anio);
        cabecera.setMes(mes);
        cabecera.setFechaHoraProcesada(LocalDateTime.now());
        cabecera.setFechaCreacion(LocalDateTime.now());
        cabecera.setUsuarioCreacion(obtenerUsuarioAutenticado());

        BigDecimal totalIngresoGeneral = BigDecimal.ZERO;
        BigDecimal totalDescuentoGeneral = BigDecimal.ZERO;
        BigDecimal salarioNetoGeneral = BigDecimal.ZERO;

        //Valida los estados del empleado si, si pueden ser procesados en planilla
        List<Empleado> empleadosActivos = empleadoRepository.findAll().stream()
                .filter(e -> e.getIdStatusEmpleado() != null && (
                        e.getIdStatusEmpleado().equals(ControlStatusEmpleado.ACTIVO.getId()) ||
                                e.getIdStatusEmpleado().equals(ControlStatusEmpleado.SUSPENDIDO_IGSS.getId()) ||
                                e.getIdStatusEmpleado().equals(ControlStatusEmpleado.SUSPENDIDO_RRHH.getId())
                ))
                .collect(Collectors.toList());

        PlanillaCabecera cabeceraGuardada = crearBase(cabecera);

        for (Empleado empleado : empleadosActivos) {
            if (empleado.getFechaContratacion().isAfter(periodo.getFechaFin())) {
                continue;
            }

            BigDecimal factorProporcional = calcularFactorProporcional(empleado.getFechaContratacion(), periodo.getFechaInicio(), periodo.getFechaFin());

            BigDecimal ingresoBase = (empleado.getIngresoSueldoBase() != null ? empleado.getIngresoSueldoBase() : BigDecimal.ZERO)
                    .multiply(factorProporcional).setScale(2, RoundingMode.HALF_UP);

            BigDecimal ingresoBono = (empleado.getIngresoBonificacionDecreto() != null ? empleado.getIngresoBonificacionDecreto() : BigDecimal.ZERO)
                    .multiply(factorProporcional).setScale(2, RoundingMode.HALF_UP);

//            BigDecimal ingresoBono = (empleado.getIngresoBonificacionDecreto() != null ? empleado.getIngresoBonificacionDecreto() : BigDecimal.ZERO);

            BigDecimal otrosIngresos = empleado.getIngresoOtrosIngresos() != null ? empleado.getIngresoOtrosIngresos() : BigDecimal.ZERO;

            BigDecimal descuentoIgss = (empleado.getDescuentoIgss() != null ? empleado.getDescuentoIgss() : BigDecimal.ZERO)
                    .multiply(factorProporcional).setScale(2, RoundingMode.HALF_UP);

            BigDecimal descuentoIsr = empleado.getDescuentoIsr() != null ? empleado.getDescuentoIsr() : BigDecimal.ZERO;

            BigDecimal descuentoInasistencias = procesarInasistencias(forzar, empleado.getIdEmpleado(), empleado.getIngresoSueldoBase(), periodo.getFechaInicio(), periodo.getFechaFin());

            BigDecimal totalIngresosEmpleado = ingresoBase.add(ingresoBono).add(otrosIngresos);
            BigDecimal totalDescuentosEmpleado = descuentoIgss.add(descuentoIsr).add(descuentoInasistencias);
            BigDecimal salarioNetoEmpleado = totalIngresosEmpleado.subtract(totalDescuentosEmpleado);

            PlanillaDetalle detalle = new PlanillaDetalle();
            detalle.setAnio(anio);
            detalle.setMes(mes);
            detalle.setIdEmpleado(empleado.getIdEmpleado());
            detalle.setFechaContratacion(empleado.getFechaContratacion());
            detalle.setIdPuesto(empleado.getIdPuesto());
            detalle.setIdStatusEmpleado(empleado.getIdStatusEmpleado());
            detalle.setIngresoSueldoBase(ingresoBase);
            detalle.setIngresoBonificacionDecreto(ingresoBono);
            detalle.setIngresoOtrosIngresos(otrosIngresos);
            detalle.setDescuentoIgss(descuentoIgss);
            detalle.setDescuentoIsr(descuentoIsr);
            detalle.setDescuentoInasistencias(descuentoInasistencias);
            detalle.setSalarioNeto(salarioNetoEmpleado);
            detalle.setFechaCreacion(LocalDateTime.now());
            detalle.setUsuarioCreacion(obtenerUsuarioAutenticado());

            planillaDetalleRepository.save(detalle);

            totalIngresoGeneral = totalIngresoGeneral.add(totalIngresosEmpleado);
            totalDescuentoGeneral = totalDescuentoGeneral.add(totalDescuentosEmpleado);
            salarioNetoGeneral = salarioNetoGeneral.add(salarioNetoEmpleado);
        }

        cabeceraGuardada.setTotalIngresos(totalIngresoGeneral);
        cabeceraGuardada.setTotalDescuentos(totalDescuentoGeneral);
        cabeceraGuardada.setSalarioNeto(salarioNetoGeneral);
        actualizarBase(cabeceraGuardada);

        return construirRespuestaDesdeBase(cabeceraGuardada);
    }

    private BigDecimal calcularFactorProporcional(LocalDate fechaContratacion, LocalDate inicioPeriodo, LocalDate finPeriodo) {
        if (fechaContratacion.isBefore(inicioPeriodo) || fechaContratacion.isEqual(inicioPeriodo)) {
            return BigDecimal.ONE;
        }

        long diasTotalesPeriodo = 30;
        long diasTrabajados = ChronoUnit.DAYS.between(fechaContratacion, finPeriodo) + 1;

        if (diasTrabajados > 30) diasTrabajados = 30;

        return new BigDecimal(diasTrabajados).divide(new BigDecimal(diasTotalesPeriodo), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal procesarInasistencias(boolean forzar, Integer idEmpleado, BigDecimal salarioBaseFull, LocalDate inicioPeriodo, LocalDate finPeriodo) {
        if (salarioBaseFull == null || salarioBaseFull.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        List<Inasistencia> inasistenciasPendientes = forzar ?
                inasistenciaRepository.findByIdEmpleado(idEmpleado) :
                inasistenciaRepository.findByIdEmpleadoAndFechaProcesadoIsNull(idEmpleado);

        BigDecimal totalDescuento = BigDecimal.ZERO;
        BigDecimal salarioDiario = salarioBaseFull.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);
        LocalDate fechaProceso = LocalDate.now();

        for (Inasistencia inasistencia : inasistenciasPendientes) {
            LocalDate incInicio = inasistencia.getFechaInicial();
            LocalDate incFin = inasistencia.getFechaFinal();

            if (!incInicio.isAfter(finPeriodo) && !incFin.isBefore(inicioPeriodo)) {

                LocalDate inicioEfectivo = incInicio.isBefore(inicioPeriodo) ? inicioPeriodo : incInicio;
                LocalDate finEfectivo = incFin.isAfter(finPeriodo) ? finPeriodo : incFin;

                long diasAusenteEnPeriodo = ChronoUnit.DAYS.between(inicioEfectivo, finEfectivo) + 1;

                if (diasAusenteEnPeriodo > 30) {
                    diasAusenteEnPeriodo = 30;
                }

                BigDecimal descuentoPeriodo = salarioDiario.multiply(new BigDecimal(diasAusenteEnPeriodo));
                totalDescuento = totalDescuento.add(descuentoPeriodo);

                if (!incFin.isAfter(finPeriodo)) {
                    inasistencia.setFechaProcesado(fechaProceso);
                    inasistencia.setFechaModificacion(LocalDateTime.now());
                    inasistencia.setUsuarioModificacion(obtenerUsuarioAutenticado());
                    inasistenciaRepository.save(inasistencia);
                }
            }
        }

        if (totalDescuento.compareTo(salarioBaseFull) > 0) {
            return salarioBaseFull;
        }

        return totalDescuento;
    }

    private PlanillaResponseDto construirRespuestaDesdeBase(PlanillaCabecera cabecera) {
        List<PlanillaDetalle> detallesEntity = planillaDetalleRepository.findByAnioAndMes(cabecera.getAnio(), cabecera.getMes());

        List<PlanillaDetalleDto> detallesDto = detallesEntity.stream().map(d -> {

            Empleado empleado = empleadoRepository.findById(d.getIdEmpleado()).
                    orElseThrow(() -> new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));

            Persona persona =  personaRepository.findById(empleado.getIdPersona()).
                    orElseThrow(() -> new BusinessException(GeneralError.ERROR_PERSONA_NOT_FOUND));

            PlanillaDetalleDto dto = new PlanillaDetalleDto();
            dto.setIdPlanillaDetalle(d.getIdPlanillaDetalle());
            dto.setIdEmpleado(d.getIdEmpleado());
            dto.setNombres(persona.getNombre().concat(" ").concat(persona.getApellido()));
            dto.setFechaContratacion(d.getFechaContratacion());
            dto.setIngresoSueldoBase(d.getIngresoSueldoBase());
            dto.setIngresoBonificacionDecreto(d.getIngresoBonificacionDecreto());
            dto.setIngresoOtrosIngresos(d.getIngresoOtrosIngresos());
            dto.setDescuentoIgss(d.getDescuentoIgss());
            dto.setDescuentoIsr(d.getDescuentoIsr());
            dto.setDescuentoInasistencias(d.getDescuentoInasistencias());
            dto.setSalarioNeto(d.getSalarioNeto());
            return dto;
        }).collect(Collectors.toList());

        PlanillaResponseDto response = new PlanillaResponseDto();
        response.setAnio(cabecera.getAnio());
        response.setMes(cabecera.getMes());
        response.setTotalIngresos(cabecera.getTotalIngresos());
        response.setTotalDescuentos(cabecera.getTotalDescuentos());
        response.setSalarioNeto(cabecera.getSalarioNeto());
        response.setFechaHoraProcesada(cabecera.getFechaHoraProcesada());
        response.setDetalles(detallesDto);

        return response;
    }
}