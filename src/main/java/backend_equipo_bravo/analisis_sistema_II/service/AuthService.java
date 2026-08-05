package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PoliticasSeguridadService politicasService;

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private JwtService jwtService;

    public Map<String,Object> login(String idUsuario, String passwordPlano, String ip, String userAgent) {

        Optional<Usuario> userOpt = usuarioRepository.findByIdUsuario(idUsuario);

        if (userOpt.isEmpty()) {
            auditoriaService.registrarIntento(idUsuario, 5, ip, userAgent, null);
            throw new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND);
        }

        Usuario usuario = userOpt.get();

        if (usuario.getIdStatusUsuario() != 1) {
            boolean isInactive = usuario.getIdStatusUsuario() == 3;

            auditoriaService.registrarIntento(idUsuario, isInactive ? 4 : 2, ip, userAgent, null);
            throw new BusinessException(isInactive ? UsuarioError.AUTH_USER_INACTIVE : UsuarioError.AUTH_USER_BLOCKED);
        }

        if (!passwordEncoder.matches(passwordPlano, usuario.getPassword())) {
            int intentosActuales = usuario.getIntentosDeAcceso() != null ? usuario.getIntentosDeAcceso() : 0;
            intentosActuales++;
            usuario.setIntentosDeAcceso(intentosActuales);

            int intentosPermitidos = politicasService.obtenerIntentosPermitidosPorSucursal(usuario.getIdSucursal());

            if (intentosActuales >= intentosPermitidos) {
                usuario.setIdStatusUsuario(2);
                usuario.setSesionActual(null);
                usuarioRepository.save(usuario);
                auditoriaService.registrarIntento(idUsuario, 3, ip, userAgent, null);
                throw new BusinessException(UsuarioError.AUTH_USER_BLOCKED_ATTEMPTS);
            } else {
                usuarioRepository.save(usuario);
                auditoriaService.registrarIntento(idUsuario, 2, ip, userAgent, null);
                throw new BusinessException(UsuarioError.AUTH_INVALID_CREDENTIALS_ATTEMPTS, intentosActuales, intentosPermitidos);
            }
        }

        usuario.setIntentosDeAcceso(0);
        usuario.setUltimaFechaIngreso(LocalDateTime.now());
        String token = jwtService.generarToken(usuario.getIdUsuario(), usuario.getIdRole());

        usuario.setSesionActual(token);
        usuarioRepository.save(usuario);

        auditoriaService.registrarIntento(idUsuario, 1, ip, userAgent, token);

        Map<String,Object> data = new HashMap<>();
        data.put("token",token);
        int numChan = usuario.getRequiereCambiarPassword();
        data.put("changePassword",numChan);

        return data;
    }
}