package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.LoginRequest;
import backend_equipo_bravo.analisis_sistema_II.dto.PasswordPolicyDto;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.CambioPasswordRecuperacionRequest;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.ValidarRespuestaRequest;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.AuthService;
import backend_equipo_bravo.analisis_sistema_II.service.PoliticasSeguridadService;
import backend_equipo_bravo.analisis_sistema_II.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para el control de acceso y seguridad de usuarios")
public class AuthController extends BaseController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PoliticasSeguridadService politicasSeguridadService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario usando credenciales y registra la IP/User-Agent para devolver el token de acceso")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        try {
            Map<String,Object> inf = authService.login(request.getIdUsuario(), request.getPassword(), ip, userAgent);

            return success(inf, SuccessCode.AUTH_LOGIN_SUCCESS);

        } catch (BusinessException e) {
            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }


    @GetMapping("/recuperacion/pregunta/{idUsuario}")
    public ResponseEntity<?> obtenerPreguntaRecuperacion(@PathVariable String idUsuario, HttpServletRequest requestHttp) {
        try {
            String ip = requestHttp.getRemoteAddr();
            String userAgent = requestHttp.getHeader("User-Agent");

            PasswordPolicyDto policyDto = politicasSeguridadService.obtenerPoliticaPasswordBase(idUsuario);

            String pregunta = usuarioService.getPreguntaUsuario(idUsuario,ip,userAgent);
            Map<String, Object> data = new HashMap<>();
            data.put("pregunta", pregunta);
            data.put("politica",policyDto);

            return success(data, SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/recuperacion/validar-respuesta")
    public ResponseEntity<?> validarRespuestaRecuperacion(
            @RequestBody ValidarRespuestaRequest requestBody,
            HttpServletRequest requestHttp) {
        try {
            String ip = requestHttp.getRemoteAddr();
            String userAgent = requestHttp.getHeader("User-Agent");

            usuarioService.validarRespuestaUsuario(
                    requestBody.getIdUsuario(),
                    requestBody.getRespuesta(),
                    ip,
                    userAgent
            );

            return successMessage("Respuesta validada correctamente. Puede proceder a cambiar su contraseña.");
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/recuperacion/cambiar-password")
    public ResponseEntity<?> cambiarPasswordRecuperacion(@RequestBody CambioPasswordRecuperacionRequest requestBody,HttpServletRequest requestHttp) {
        try {
            String ip = requestHttp.getRemoteAddr();
            String userAgent = requestHttp.getHeader("User-Agent");

            usuarioService.validarRespuestaUsuario(
                    requestBody.getIdUsuario(),
                    requestBody.getRespuesta(),
                    ip,
                    userAgent
            );

            usuarioService.cambioPasswordRecuperacion(requestBody.getIdUsuario(), requestBody.getPassword());
            return successMessage("Contraseña actualizada exitosamente. Ahora puede iniciar sesión.");
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}