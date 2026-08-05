package backend_equipo_bravo.analisis_sistema_II.config;

import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.repository.UsuarioRepository;
import backend_equipo_bravo.analisis_sistema_II.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            String idUsuario = jwtService.extraerUsuario(token);

            if (idUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario).orElse(null);

                if (usuario != null) {
                    boolean tokenCoincide = token.equals(usuario.getSesionActual());
                    boolean estaActivo = usuario.getIdStatusUsuario() == 1;

                    if (!estaActivo) {
                        boolean isInactivo = usuario.getIdStatusUsuario() == 3;

                        enviarErrorPersonalizado(response, HttpServletResponse.SC_FORBIDDEN, 1501,
                                isInactivo ? "USUARIO_INACTIVO" : "USUARIO_BLOQUEADO",
                                isInactivo ? "Tu usuario está inactivo." : "Tu usuario está bloqueado.");
                        return;
                    }

                    if (!tokenCoincide) {
                        enviarErrorPersonalizado(response, HttpServletResponse.SC_UNAUTHORIZED, 401,
                                "SESION_INVALIDA", "Tu sesión ya no es válida o iniciaste sesión en otro dispositivo.");
                        return;
                    }

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            usuario.getIdUsuario(), null, new ArrayList<>()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            enviarErrorPersonalizado(response, HttpServletResponse.SC_UNAUTHORIZED, 401,
                    "TOKEN_INVALIDO", "El token ha expirado o es incorrecto.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void enviarErrorPersonalizado(HttpServletResponse response, int httpStatus, int codigoNumerico, String codigoTexto, String mensaje) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus);

        Map<String, Object> body = new HashMap<>();
        body.put("exito", false);
        body.put("codigoNumerico", codigoNumerico);
        body.put("codigoTexto", codigoTexto);
        body.put("mensaje", mensaje);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(body));
    }
}