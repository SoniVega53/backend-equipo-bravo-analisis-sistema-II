package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.PersonaRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Persona;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.PersonaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/persona")
@Tag(name = "Persona", description = "Endpoints para CRUD persona")
public class PersonaController extends BaseController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(personaService.buscarTodosPermisos(), SuccessCode.PERSONA_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(personaService.buscarPorIdPermisos(id), SuccessCode.PERSONA_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PersonaRequest request) {
        try {
            Persona creado = personaService.crear(request);
            Map<String, Object> data = Map.of("persona", creado);
            return success(data, SuccessCode.PERSONA_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody PersonaRequest request) {
        try {
            Persona actualizado = personaService.actualizar(id, request);
            Map<String, Object> data = Map.of("persona", actualizado);
            return success(data, SuccessCode.PERSONA_UPDATED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            personaService.eliminarBasePermisos(id);
            return success(null, SuccessCode.PERSONA_DELETED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), ex.getCodigoNumerico() == 409 ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND);
        }
    }
}
