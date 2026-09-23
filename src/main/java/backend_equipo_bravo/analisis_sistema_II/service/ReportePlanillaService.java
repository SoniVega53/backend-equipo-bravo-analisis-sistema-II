package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaDetalleDto;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaResponseDto;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.ReportePlanillaRequestDto;
import backend_equipo_bravo.analisis_sistema_II.entity.*;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportePlanillaService extends BaseService<PlanillaCabecera, Object> {

    @Autowired
    private PlanillaCabeceraRepository planillaCabeceraRepository;
    @Autowired
    private PlanillaDetalleRepository planillaDetalleRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private StatusEmpleadoRepository statusEmpleadoRepository;

    @Autowired
    private PuestoRepository puestoRepository;

    public ReportePlanillaService() {
        setEntityName("ReportePlanilla");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected JpaRepository<PlanillaCabecera, Object> getRepository() {
        return (JpaRepository<PlanillaCabecera, Object>) (JpaRepository<?, ?>) planillaCabeceraRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Object id) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    public PlanillaResponseDto obtenerReporte(ReportePlanillaRequestDto request) {
        validarConsulta();

        PlanillaCabecera cabecera = planillaCabeceraRepository.findByAnioAndMes(request.getAnio(), request.getMes())
                .orElseThrow(() -> new BusinessException(GeneralError.PLANILLA_NOT_FOUND));

        return construirRespuestaDesdeBase(cabecera);
    }

    private PlanillaResponseDto construirRespuestaDesdeBase(PlanillaCabecera cabecera) {
        List<PlanillaDetalle> detallesEntity = planillaDetalleRepository.findByAnioAndMes(cabecera.getAnio(), cabecera.getMes());

        List<PlanillaDetalleDto> detallesDto = detallesEntity.stream().map(d -> {
            Empleado empleado = empleadoRepository.findById(d.getIdEmpleado())
                    .orElseThrow(() -> new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));

            Persona persona = personaRepository.findById(empleado.getIdPersona())
                    .orElseThrow(() -> new BusinessException(GeneralError.ERROR_PERSONA_NOT_FOUND));

            Puesto puesto = puestoRepository.findById(empleado.getIdPuesto())
                    .orElseThrow(() -> new BusinessException(GeneralError.ERROR_PUESTO_NOT_FOUND));

            StatusEmpleado statusEmpleado = statusEmpleadoRepository.findById(empleado.getIdStatusEmpleado())
                    .orElseThrow(() -> new BusinessException(GeneralError.ERROR_STATUS_NOT_FOUND));

            PlanillaDetalleDto dto = new PlanillaDetalleDto();
            dto.setIdPlanillaDetalle(d.getIdPlanillaDetalle());
            dto.setIdEmpleado(d.getIdEmpleado());
            dto.setNombres(persona.getNombre().concat(" ").concat(persona.getApellido()));
            dto.setPuesto(puesto.getNombre());
            dto.setStatus(statusEmpleado.getNombre());
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