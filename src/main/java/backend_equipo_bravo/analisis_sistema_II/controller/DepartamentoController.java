package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.DepartamentoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Departamento;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.DepartamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/departamento")
@Tag(name = "Departamento", description = "Endpoints para CRUD departamento")
public class DepartamentoController extends BaseController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(departamentoService.buscarTodosPermisos(), SuccessCode.DEPARTAMENTO_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(departamentoService.buscarPorIdPermisos(id), SuccessCode.DEPARTAMENTO_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody DepartamentoRequest request) {
        try {
            Departamento creado = departamentoService.crear(request);
            Map<String, Object> data = Map.of("departamento", creado);
            return success(data, SuccessCode.DEPARTAMENTO_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody DepartamentoRequest request) {
        try {
            Departamento actualizado = departamentoService.actualizar(id, request);
            Map<String, Object> data = Map.of("departamento", actualizado);
            return success(data, SuccessCode.DEPARTAMENTO_UPDATED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            departamentoService.eliminarBasePermisos(id);
            return success(null, SuccessCode.DEPARTAMENTO_DELETED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), ex.getCodigoNumerico() == 409 ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND);
        }
    }
}
