package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.ModuloRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Modulo;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.ModuloService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/modulo")
@Tag(name = "Modulo", description = "Endpoints para CRUD de Modulos")
public class ModuloController extends BaseController {

    @Autowired
    private ModuloService moduloService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(moduloService.buscarTodosPermisos(), SuccessCode.MODULO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(moduloService.buscarPorIdPermisos(id), SuccessCode.MODULO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ModuloRequest modulo) {
        try {
            Modulo creado = moduloService.crear(modulo);
            Map<String, Object> data = Map.of("modulo", creado);
            return success(data, SuccessCode.MODULO_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody ModuloRequest modulo) {
        try {
            Modulo actualizado = moduloService.actualizar(id, modulo);
            Map<String, Object> data = Map.of("modulo", actualizado);
            return success(data, SuccessCode.MODULO_UPDATED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            moduloService.eliminarBasePermisos(id);
            return success(null, SuccessCode.MODULO_DELETED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}