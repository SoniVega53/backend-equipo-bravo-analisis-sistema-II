package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.OpcionRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Opcion;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.OpcionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/opcion")
@Tag(name = "Opcion", description = "Endpoints para CRUD de Opciones")
public class OpcionController extends BaseController {

    @Autowired
    private OpcionService opcionService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(opcionService.buscarTodosPermisos(), SuccessCode.OPCION_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(opcionService.buscarPorIdPermisos(id), SuccessCode.OPCION_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody OpcionRequest opcion) {
        try {
            Opcion creada = opcionService.crear(opcion);
            Map<String, Object> data = Map.of("opcion", creada);
            return success(data, SuccessCode.OPCION_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody OpcionRequest opcion) {
        try {
            Opcion actualizada = opcionService.actualizar(id, opcion);
            Map<String, Object> data = Map.of("opcion", actualizada);
            return success(data, SuccessCode.OPCION_UPDATED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            opcionService.eliminarBasePermisos(id);
            return success(null, SuccessCode.OPCION_DELETED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}