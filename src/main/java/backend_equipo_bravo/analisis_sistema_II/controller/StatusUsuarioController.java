package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.StatusUsuarioRequest;
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
@RequestMapping("/api/console/status-usuario")
@Tag(name = "Status Usuario", description = "Endpoints para CRUD Status Usuario")
public class StatusUsuarioController extends BaseController {

    @Autowired
    private StatusUsuarioService statusUsuarioService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(statusUsuarioService.buscarTodosPermisos(), SuccessCode.STATUS_USUARIO_GENERAL);
        }catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(statusUsuarioService.buscarPorIdPermisos(id), SuccessCode.STATUS_USUARIO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody StatusUsuarioRequest request) {
        try {
            StatusUsuario creada = statusUsuarioService.crear(request);
            Map<String, Object> data = Map.of("status-usuario", creada);
            return success(data, SuccessCode.STATUS_USUARIO_SUCCESS);
        }catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody StatusUsuarioRequest request) {
        try {
            StatusUsuario actualizada = statusUsuarioService.actualizar(id, request);
            Map<String, Object> data = Map.of("status-usuario", actualizada);
            return success(data, SuccessCode.STATUS_USUARIO_UPDATED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            statusUsuarioService.eliminarBasePermisos(id);
            return success(null, SuccessCode.STATUS_USUARIO_DELETED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}