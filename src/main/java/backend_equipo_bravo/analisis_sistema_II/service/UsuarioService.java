package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.usuario.UsuarioUpdateRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario actualizarUsuario(String idUsuarioActualizar, UsuarioUpdateRequest request) {
        String idUsuarioEjecutor = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario ejecutor = usuarioRepository.findByIdUsuario(idUsuarioEjecutor)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        boolean isAdmin = ejecutor.getIdRole() == 1;

        if (!idUsuarioEjecutor.equals(idUsuarioActualizar) && !isAdmin) {
            throw new BusinessException(UsuarioError.AUTH_UNAUTHORIZED);
        }

        boolean intentaCambiarSensibles = request.getIdRole() != null ||
                request.getIdStatusUsuario() != null ||
                request.getIntentosDeAcceso() != null;

        if (intentaCambiarSensibles && !isAdmin) {
            throw new BusinessException(UsuarioError.AUTH_UNAUTHORIZED);
        }

        Usuario usuarioDestino = usuarioRepository.findByIdUsuario(idUsuarioActualizar)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        if (request.getNombre() != null) usuarioDestino.setNombre(request.getNombre());
        if (request.getApellido() != null) usuarioDestino.setApellido(request.getApellido());
        if (request.getFechaNacimiento() != null) usuarioDestino.setFechaNacimiento(request.getFechaNacimiento());
        if (request.getCorreoElectronico() != null) usuarioDestino.setCorreoElectronico(request.getCorreoElectronico());
        if (request.getTelefonoMovil() != null) usuarioDestino.setTelefonoMovil(request.getTelefonoMovil());
        if (request.getIdSucursal() != null) usuarioDestino.setIdSucursal(request.getIdSucursal());

        if (request.getIdRole() != null) usuarioDestino.setIdRole(request.getIdRole());
        if (request.getIdStatusUsuario() != null) usuarioDestino.setIdStatusUsuario(request.getIdStatusUsuario());
        if (request.getIntentosDeAcceso() != null) usuarioDestino.setIntentosDeAcceso(request.getIntentosDeAcceso());

        // Auditoría automática
        usuarioDestino.setFechaModificacion(LocalDateTime.now());
        usuarioDestino.setUsuarioModificacion(idUsuarioEjecutor);

        return usuarioRepository.save(usuarioDestino);
    }
}