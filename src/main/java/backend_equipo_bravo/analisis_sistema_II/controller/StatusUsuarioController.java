package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.statususuario.StatusUsuarioRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.StatusUsuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.StatusUsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/status-usuario")
@Tag(name = "StatusUsuario", description = "Endpoints para CRUD de estatus de usuario")
public class StatusUsuarioController extends BaseController {

    @Autowired
    private StatusUsuarioService statusUsuarioService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        return success(
                statusUsuarioService.buscarTodos(),
                SuccessCode.STATUS_USUARIO_GENERAL
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(
                    statusUsuarioService.buscarPorId(id),
                    SuccessCode.STATUS_USUARIO_GENERAL
            );
        } catch (BusinessException e) {
            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody StatusUsuarioRequest statusUsuarioRequest) {

        StatusUsuario creado =
                statusUsuarioService.crear(statusUsuarioRequest);

        Map<String, Object> data = Map.of(
                "statusUsuario", creado
        );

        return success(
                data,
                SuccessCode.STATUS_USUARIO_SUCCESS
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody StatusUsuarioRequest statusUsuarioRequest) {

        try {
            StatusUsuario actualizado =
                    statusUsuarioService.actualizar(
                            id,
                            statusUsuarioRequest
                    );

            Map<String, Object> data = Map.of(
                    "statusUsuario", actualizado
            );

            return success(
                    data,
                    SuccessCode.STATUS_USUARIO_UPDATED_SUCCESS
            );

        } catch (BusinessException e) {
            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {

        try {
            statusUsuarioService.eliminarBase(id);

            return success(
                    null,
                    SuccessCode.STATUS_USUARIO_DELETED_SUCCESS
            );

        } catch (BusinessException e) {
            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}