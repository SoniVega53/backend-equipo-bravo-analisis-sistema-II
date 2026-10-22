package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.flujostatusempleado.FlujoStatusEmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.dto.flujostatusempleado.FlujoStatusEmpleadoResponseDto;
import backend_equipo_bravo.analisis_sistema_II.entity.FlujoStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.entity.StatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.FlujoStatusEmpleadoError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.FlujoStatusEmpleadoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.StatusEmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlujoStatusEmpleadoService extends BaseService<FlujoStatusEmpleado, FlujoStatusEmpleadoId> {

    @Autowired
    private FlujoStatusEmpleadoRepository flujoStatusEmpleadoRepository;

    @Autowired
    private StatusEmpleadoRepository statusEmpleadoRepository;

    @Override
    protected JpaRepository<FlujoStatusEmpleado, FlujoStatusEmpleadoId> getRepository() {
        return flujoStatusEmpleadoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(FlujoStatusEmpleadoId id) {
        return new BusinessException(FlujoStatusEmpleadoError.FLUJO_NOT_FOUND);
    }

    public FlujoStatusEmpleado crear(FlujoStatusEmpleadoRequest request) {
        validarRequest(request);

        FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(request.getIdStatusActual(), request.getIdStatusNuevo());
        if (flujoStatusEmpleadoRepository.existsById(id)) {
            throw new BusinessException(FlujoStatusEmpleadoError.FLUJO_ALREADY_EXISTS);
        }

        String usuarioActual = obtenerUsuarioAutenticado();

        FlujoStatusEmpleado flujo = new FlujoStatusEmpleado();
        flujo.setIdStatusActual(request.getIdStatusActual());
        flujo.setIdStatusNuevo(request.getIdStatusNuevo());
        flujo.setNombreEvento(request.getNombreEvento().trim());
        flujo.setUsuarioCreacion(usuarioActual);
        flujo.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(flujo);
    }

    public FlujoStatusEmpleado actualizar(FlujoStatusEmpleadoId id, FlujoStatusEmpleadoRequest request) {
        FlujoStatusEmpleado existente = super.buscarPorId(id);

        if (request == null || request.getNombreEvento() == null || request.getNombreEvento().trim().isEmpty()) {
            throw new BusinessException(FlujoStatusEmpleadoError.NOMBRE_EVENTO_REQUERIDO);
        }

        String usuarioActual = obtenerUsuarioAutenticado();
        existente.setNombreEvento(request.getNombreEvento().trim());
        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    public List<FlujoStatusEmpleadoResponseDto> listarDetalladosPermisos() {
        validarConsulta();
        Map<Integer, String> mapaStatus = statusEmpleadoRepository.findAll().stream()
                .collect(Collectors.toMap(StatusEmpleado::getIdStatusEmpleado, StatusEmpleado::getNombre, (a, b) -> a));

        return flujoStatusEmpleadoRepository.findAll().stream()
                .map(flujo -> FlujoStatusEmpleadoResponseDto.builder()
                        .idStatusActual(flujo.getIdStatusActual())
                        .nombreStatusActual(mapaStatus.getOrDefault(flujo.getIdStatusActual(), "ID: " + flujo.getIdStatusActual()))
                        .idStatusNuevo(flujo.getIdStatusNuevo())
                        .nombreStatusNuevo(mapaStatus.getOrDefault(flujo.getIdStatusNuevo(), "ID: " + flujo.getIdStatusNuevo()))
                        .nombreEvento(flujo.getNombreEvento())
                        .fechaCreacion(flujo.getFechaCreacion())
                        .usuarioCreacion(flujo.getUsuarioCreacion())
                        .fechaModificacion(flujo.getFechaModificacion())
                        .usuarioModificacion(flujo.getUsuarioModificacion())
                        .build())
                .collect(Collectors.toList());
    }

    public List<FlujoStatusEmpleado> buscarPorStatusActualPermisos(Integer idStatusActual) {
        validarConsulta();
        return flujoStatusEmpleadoRepository.findByIdStatusActual(idStatusActual);
    }

    private void validarRequest(FlujoStatusEmpleadoRequest request) {
        if (request == null || request.getIdStatusActual() == null || request.getIdStatusNuevo() == null) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "Debe seleccionar un estatus actual y un estatus nuevo.");
        }

        if (request.getIdStatusActual().equals(request.getIdStatusNuevo())) {
            throw new BusinessException(FlujoStatusEmpleadoError.MISMO_STATUS);
        }

        if (request.getNombreEvento() == null || request.getNombreEvento().trim().isEmpty()) {
            throw new BusinessException(FlujoStatusEmpleadoError.NOMBRE_EVENTO_REQUERIDO);
        }

        if (!statusEmpleadoRepository.existsById(request.getIdStatusActual())) {
            throw new BusinessException(FlujoStatusEmpleadoError.STATUS_ACTUAL_NOT_FOUND);
        }

        if (!statusEmpleadoRepository.existsById(request.getIdStatusNuevo())) {
            throw new BusinessException(FlujoStatusEmpleadoError.STATUS_NUEVO_NOT_FOUND);
        }
    }
}

