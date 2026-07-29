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

import java.io.IOException;
import java.util.ArrayList;

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

                    if (tokenCoincide && estaActivo) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                usuario.getIdUsuario(), null, new ArrayList<>()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e) {
        }

        filterChain.doFilter(request, response);
    }
}