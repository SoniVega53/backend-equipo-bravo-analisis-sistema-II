package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.PasswordPolicyDto;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.*;
import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.PoliticasSeguridadService;
import backend_equipo_bravo.analisis_sistema_II.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        try {
            return success(usuarioService.buscarTodos(), SuccessCode.GENERO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/console/eliminar/{idUsuario}")
    public ResponseEntity<?> eliminar(@PathVariable String idUsuario) {
        try {
            usuarioService.eliminarUsuario(idUsuario);
            return success("Se elimino correctamente", SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{id}/fotografia", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Actualizar fotografía", description = "Guarda la foto como BYTEA en BD y retorna el Base64")
    public ResponseEntity<Map<String, Object>> actualizarFotografia(
            @Parameter(description = "ID del usuario (String)") @PathVariable String id,
            @Parameter(description = "Archivo de imagen a subir") @RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return error(400, "FILE_EMPTY", "El archivo de imagen está vacío", HttpStatus.BAD_REQUEST);
            }

            String base64Url = usuarioService.actualizarFotografia(id, file);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("nuevaUrl", base64Url);
            responseData.put("mensaje", "Fotografía actualizada correctamente");

            return success(responseData, SuccessCode.GENERAL);

        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return error(500, "INTERNAL_ERROR", "Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fotografia")
    public ResponseEntity<Map<String, Object>> obtenerFotografia() {
        try {
            String base64Url = usuarioService.obtenerFotografiaBase64();
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("fotografia", base64Url);
            return success(responseData, SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return error(500, "INTERNAL_ERROR", "Error al obtener la imagen: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
                    usuarioService.buscarTodos(),
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarv2(
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

}