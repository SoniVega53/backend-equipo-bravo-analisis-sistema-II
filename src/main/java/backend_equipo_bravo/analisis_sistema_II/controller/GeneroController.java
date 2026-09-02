package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.genero.GeneroRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Genero;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.GeneroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/genero")
@Tag(name = "Genero", description = "Endpoints para CRUD genero")
public class GeneroController extends BaseController {
    @Autowired
    private GeneroService generoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(generoService.buscarTodosPermisos(), SuccessCode.GENERO_GENERAL);
        }catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(generoService.buscarPorIdPermisos(id), SuccessCode.GENERO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody GeneroRequest genero) {
        try{
            Genero creado = generoService.crear(genero);

            Map<String, Object> data = Map.of("genero", creado);
            return success(data, SuccessCode.GENERO_SUCCESS);
        }catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody GeneroRequest genero) {
        try {
            Genero actualizado = generoService.actualizar(id,genero);
            Map<String, Object> data = Map.of("genero", actualizado);
            return success(data, SuccessCode.GENERO_UPDATED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            generoService.eliminarBasePermisos(id);
            return success(null, SuccessCode.GENERO_DELETED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
