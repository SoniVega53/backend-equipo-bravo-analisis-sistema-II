package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.PasswordPolicyDto;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.*;
import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.PoliticasSeguridadService;
import backend_equipo_bravo.analisis_sistema_II.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Boolean changePassword = usuarioService.getChangePassword();

            Map<String, Object> data = new HashMap<>();
            data.put("changePassword", changePassword);
            if (changePassword) {
                data.put("policy",policyDto);
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

    @PostMapping("/console/guardar")
    public ResponseEntity<?> consoleUpdateAndSaveUsuario(@RequestBody UsuarioSaveRequest request) {
        try {
            Usuario usuario = usuarioService.consoleUpdateAndSaveUsuario(request);
            return success(usuario, SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/console/listar")
    public ResponseEntity<?> listarTodos() {
        return success(usuarioService.buscarTodos(), SuccessCode.GENERO_GENERAL);
    }

    @DeleteMapping("/console/eliminar/{idUsuario}")
    public ResponseEntity<?> eliminar(@PathVariable String idUsuario) {
        usuarioService.eliminarUsuario(idUsuario);
        return success("Se elimino correctamente", SuccessCode.GENERO_GENERAL);
    }
}