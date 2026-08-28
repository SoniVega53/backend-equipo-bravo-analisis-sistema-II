package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.PasswordPolicyDto;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.UsuarioPasswordChangeRequest;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.UsuarioPerfilRequest;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.UsuarioUpdatePasswordRequest;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.UsuarioUpdateRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.PoliticasSeguridadService;
import backend_equipo_bravo.analisis_sistema_II.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.UsuarioCreateRequest;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController extends BaseController{

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PoliticasSeguridadService empresaService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
        @Valid @RequestBody UsuarioCreateRequest request) {

    try {
        Usuario usuario = usuarioService.crearUsuario(request);

        Map<String, Object> data = Map.of(
                "usuario", usuario.getIdUsuario()
        );

        return success(data, SuccessCode.USER_UPDATED_SUCCESS);

    } catch (BusinessException e) {
        return error(
                e.getCodigoNumerico(),
                e.getCodigoTexto(),
                e.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {

    try {
        return success(
                usuarioService.listarUsuarios(),
                SuccessCode.AUTH_LOGIN_SUCCESS
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

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscar(
        @PathVariable("id") String idUsuario) {

    try {
        Usuario usuario = usuarioService.buscarUsuario(idUsuario);

        return success(
                usuario,
                SuccessCode.AUTH_LOGIN_SUCCESS
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

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable("id") String idUsuario, @RequestBody UsuarioUpdateRequest request) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(idUsuario, request);

            Map<String, Object> data = Map.of("usuario", usuarioActualizado.getIdUsuario());
            return success(data, SuccessCode.USER_UPDATED_SUCCESS);

        } catch (BusinessException e) {
            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(
        @PathVariable("id") String idUsuario) {

    try {
        usuarioService.eliminarUsuario(idUsuario);

        return success(
                "Usuario eliminado correctamente",
                SuccessCode.AUTH_LOGIN_SUCCESS
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

    @PutMapping("/primerIngreso")
    public ResponseEntity<Map<String, Object>> actualizarPrimerIngreso(@RequestBody UsuarioUpdatePasswordRequest request) {
        try {
            usuarioService.actualizarPrimerIngreso(request.getPassword());
            return success("", SuccessCode.USER_UPDATED_PASS_SUCCESS);
        } catch (BusinessException e) {
            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @GetMapping("/esChangePassword")
    public ResponseEntity<Map<String, Object>> getChangePassword() {
        try {
            PasswordPolicyDto policyDto = empresaService.obtenerPoliticaPassword();
            int changePassword = usuarioService.getChangePassword();

            Map<String, Object> data = new HashMap<>();
            data.put("changePassword", changePassword);
            if (changePassword > 0) {
                data.put("policy",policyDto);
                data.put("textoIngresoPassword", changePassword == 1 ?
                        "Por favor, configure su nueva contraseña." :
                        "Tu contraseña ha vencido. Por favor, ingresa una nueva para continuar."
                        );
            }
            return success(data, SuccessCode.AUTH_LOGIN_SUCCESS);
        } catch (BusinessException e) {
            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Map<String, Object>> cambioPassword(@RequestBody UsuarioPasswordChangeRequest request, HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        try {
            PasswordPolicyDto policyDto = empresaService.obtenerPoliticaPassword();

            if (!validarPasswordConRegex(request.getPasswordNew(),policyDto.getRegex())) {
                throw new BusinessException(UsuarioError.AUTH_INVALID_POLICY);
            }

            usuarioService.cambioPassword(request.getPasswordOld(), request.getPasswordNew(),ip,userAgent);

            return success("Cambio Exitoso", SuccessCode.AUTH_LOGIN_SUCCESS);
        } catch (BusinessException e) {
            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> getDataPerfil() {
        try {
            return success(usuarioService.getDataPerfil(), SuccessCode.AUTH_PERFIL_SUCCESS);
        } catch (BusinessException e) {
            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @PutMapping("/perfilUpdate")
    public ResponseEntity<Map<String, Object>> putDataPerfil(@RequestBody UsuarioPerfilRequest request) {
        try {
            return success(usuarioService.actualizarPerfil(request), SuccessCode.AUTH_PERFIL_SUCCESS);
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