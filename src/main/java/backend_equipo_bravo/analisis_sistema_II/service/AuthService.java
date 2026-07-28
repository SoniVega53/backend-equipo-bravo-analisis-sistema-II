package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public String login(String idUsuario, String passwordPlano, String ip, String userAgent) {

        Optional<Usuario> userOpt = usuarioRepository.findByIdUsuario(idUsuario);

        if (userOpt.isEmpty()) {
            auditoriaService.registrarIntento(idUsuario, 5, ip, userAgent, null); // 5 = Usuario no existe
            throw new RuntimeException("Credenciales inválidas");
        }

        Usuario usuario = userOpt.get();

        if (usuario.getIdStatusUsuario() != 1) {
            auditoriaService.registrarIntento(idUsuario, 4, ip, userAgent, null); // 4 = Usuario inactivo
            throw new RuntimeException("El usuario se encuentra inactivo o bloqueado. Contacte al administrador.");
        }

        if (!passwordEncoder.matches(passwordPlano, usuario.getPassword())) {
            int intentosActuales = usuario.getIntentosDeAcceso() != null ? usuario.getIntentosDeAcceso() : 0;
            intentosActuales++;
            usuario.setIntentosDeAcceso(intentosActuales);

            int intentosPermitidos = politicasService.obtenerIntentosPermitidosPorSucursal(usuario.getIdSucursal());

            if (intentosActuales >= intentosPermitidos) {
                usuario.setIdStatusUsuario(2);
                usuarioRepository.save(usuario);
                auditoriaService.registrarIntento(idUsuario, 3, ip, userAgent, null); // 3 = Bloqueado por intentos
                throw new RuntimeException("Su cuenta ha sido bloqueada por exceder el número de intentos configurados.");
            } else {
                usuarioRepository.save(usuario);
                auditoriaService.registrarIntento(idUsuario, 2, ip, userAgent, null); // 2 = Password incorrecto
                throw new RuntimeException("Credenciales inválidas. Intento " + intentosActuales + " de " + intentosPermitidos + ".");
            }
        }

        usuario.setIntentosDeAcceso(0);
        usuario.setUltimaFechaIngreso(LocalDateTime.now());
        String tokenSimulado = "jwt-token-generado-aqui";
        usuario.setSesionActual(tokenSimulado);
        usuarioRepository.save(usuario);

        auditoriaService.registrarIntento(idUsuario, 1, ip, userAgent, tokenSimulado); // 1 = Acceso concedido

        return tokenSimulado;
    }
}