package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.statususuario.StatusUsuarioRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.StatusUsuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.StatusUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StatusUsuarioService extends BaseService<StatusUsuario, Integer> {

    @Autowired
    private StatusUsuarioRepository statusUsuarioRepository;

    @Override
    protected JpaRepository<StatusUsuario, Integer> getRepository() {
        return statusUsuarioRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    public StatusUsuario crear(StatusUsuarioRequest request) {

        String usuarioActual = obtenerUsuarioAutenticado();

        StatusUsuario statusUsuario = new StatusUsuario();

        statusUsuario.setNombre(request.getNombre());
        statusUsuario.setUsuarioCreacion(usuarioActual);
        statusUsuario.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(statusUsuario);
    }

    public StatusUsuario actualizar(
            Integer id,
            StatusUsuarioRequest statusUsuarioActualizado) {

        StatusUsuario existente = super.buscarPorId(id);

        String usuarioActual = obtenerUsuarioAutenticado();

        existente.setNombre(statusUsuarioActualizado.getNombre());
        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }
}