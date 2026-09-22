package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.inasistencia.InasistenciaRequestDto;
import backend_equipo_bravo.analisis_sistema_II.dto.inasistencia.InasistenciaResponseDto;
import backend_equipo_bravo.analisis_sistema_II.entity.Empleado;
import backend_equipo_bravo.analisis_sistema_II.entity.Inasistencia;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.EmpleadoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.InasistenciaRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InasistenciaService extends BaseService<Inasistencia, Integer> {

    @Autowired
    private InasistenciaRepository inasistenciaRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    protected JpaRepository<Inasistencia, Integer> getRepository() {
        return inasistenciaRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    @Transactional(readOnly = true)
    public List<InasistenciaResponseDto> listarTodasPermisos() {
        return buscarTodosPermisos().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<InasistenciaResponseDto> listarTodasEmpleadoPermisos(Integer idEmpleado) {
        validarConsulta();
        return inasistenciaRepository.findByIdEmpleado(idEmpleado).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InasistenciaResponseDto crearInasistencia(InasistenciaRequestDto request) {
        validarRangoFechas(request);

        Empleado empleado = empleadoRepository.findById(request.getIdEmpleado())
                .orElseThrow(() -> new BusinessException(GeneralError.ERROR_SERVICE));

        Inasistencia inasistencia = new Inasistencia();
        inasistencia.setIdEmpleado(empleado.getIdEmpleado());
        inasistencia.setFechaInicial(request.getFechaInicio());
        inasistencia.setFechaFinal(request.getFechaFin());
        inasistencia.setMotivoInasistencia(request.getMotivoInasistencia());
        inasistencia.setFechaProcesado(null);

        inasistencia.setFechaCreacion(LocalDateTime.now());
        inasistencia.setUsuarioCreacion(obtenerUsuarioAutenticado());

        Inasistencia guardada = crearBasePermisos(inasistencia);
        return mapToResponseDto(guardada);
    }

    @Transactional
    public InasistenciaResponseDto actualizarInasistencia(Integer id, InasistenciaRequestDto request) {
        Inasistencia inasistencia = buscarPorId(id);

        if (inasistencia.getFechaProcesado() != null) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }

        validarRangoFechas(request);

        inasistencia.setFechaInicial(request.getFechaInicio());
        inasistencia.setFechaFinal(request.getFechaFin());
        inasistencia.setMotivoInasistencia(request.getMotivoInasistencia());

        inasistencia.setFechaModificacion(LocalDateTime.now());
        inasistencia.setUsuarioModificacion(obtenerUsuarioAutenticado());

        Inasistencia actualizada = actualizarBasePermisos(inasistencia);
        return mapToResponseDto(actualizada);
    }

    @Transactional
    public String eliminarInasistencia(Integer id) {
        Inasistencia inasistencia = buscarPorId(id);

        if (inasistencia.getFechaProcesado() != null) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }

        eliminarBasePermisos(id);
        return "Eliminado Correctamente";
    }

    private void validarRangoFechas(InasistenciaRequestDto request) {
        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }
    }

    private InasistenciaResponseDto mapToResponseDto(Inasistencia entity) {
        InasistenciaResponseDto dto = new InasistenciaResponseDto();
        dto.setIdInasistencia(entity.getIdInasistencia());
        dto.setIdEmpleado(entity.getIdEmpleado());
        dto.setFechaInicio(entity.getFechaInicial());
        dto.setFechaFin(entity.getFechaFinal());
        dto.setMotivoInasistencia(entity.getMotivoInasistencia());
        dto.setFechaProcesado(entity.getFechaProcesado());
        dto.setProcesado(entity.getFechaProcesado() != null);
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setUsuarioCreacio(entity.getUsuarioCreacion());

        empleadoRepository.findById(entity.getIdEmpleado()).ifPresent(emp -> {
            personaRepository.findById(emp.getIdPersona()).ifPresent(p -> {
                dto.setNombreEmpleado(p.getNombre() + " " + p.getApellido());
            });
        });

        return dto;
    }
}