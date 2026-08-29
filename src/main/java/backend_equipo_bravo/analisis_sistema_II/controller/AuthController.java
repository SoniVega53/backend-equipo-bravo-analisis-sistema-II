package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.LoginRequest;
import backend_equipo_bravo.analisis_sistema_II.dto.PasswordPolicyDto;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.*;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
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
            Map<String, Object> inf = authService.login(request.getIdUsuario(), request.getPassword(), ip, userAgent);

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

            String pregunta = usuarioService.getPreguntaUsuario(idUsuario, ip, userAgent);
            Map<String, Object> data = new HashMap<>();
            data.put("pregunta", pregunta);
            data.put("politica", policyDto);

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
    public ResponseEntity<?> cambiarPasswordRecuperacion(@RequestBody CambioPasswordRecuperacionRequest requestBody, HttpServletRequest requestHttp) {
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

    @GetMapping("/pregunta/{idUsuario}")
    public ResponseEntity<UsuarioPreguntaResponse> obtenerPregunta(
            @PathVariable String idUsuario
    ) {

        try {

            UsuarioPreguntaResponse response =
                    usuarioService.obtenerPregunta(
                            idUsuario.trim()
                    );

            return ResponseEntity.ok(response);

        } catch (BusinessException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @PostMapping("/validar-respuesta")
    public ResponseEntity<Boolean> validarRespuesta(
            @RequestBody ValidarPreguntasRequest request
    ) {

        boolean valida =
                usuarioService.validarRespuesta(request);

        return ResponseEntity.ok(valida);
    }

    @GetMapping("/password-policy")
    public ResponseEntity<PasswordPolicyDto> obtenerPoliticaPassword(
            @RequestParam String idUsuario
    ) {

        PasswordPolicyDto policyDto =
                politicasSeguridadService.obtenerPoliticaPasswordPorUsuario(
                        idUsuario
                );

        return ResponseEntity.ok(policyDto);
    }

    @PutMapping("/recuperar-password")
    public ResponseEntity<Map<String, Object>> recuperarPassword(
            @RequestBody RecuperarPasswordUpdateRequest request
    ) {

        try {

            PasswordPolicyDto policyDto =
                    politicasSeguridadService.obtenerPoliticaPasswordPorUsuario(
                            request.getIdUsuario()
                    );

            if (!validarPasswordConRegex(
                    request.getPassword(),
                    policyDto.getRegex()
            )) {

                throw new BusinessException(
                        UsuarioError.AUTH_INVALID_POLICY
                );
            }

            usuarioService.recuperarPassword(request);

            return success(
                    "",
                    SuccessCode.USER_UPDATED_PASS_SUCCESS
            );

        } catch (BusinessException e) {

            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}