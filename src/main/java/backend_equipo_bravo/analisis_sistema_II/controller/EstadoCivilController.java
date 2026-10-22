package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.EstadoCivilRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.EstadoCivil;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.EstadoCivilService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/estado-civil")
@Tag(name = "Estado Civil", description = "Endpoints para CRUD estado civil")
public class EstadoCivilController extends BaseController {

    @Autowired
    private EstadoCivilService estadoCivilService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(estadoCivilService.buscarTodosPermisos(), SuccessCode.ESTADO_CIVIL_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(estadoCivilService.buscarPorIdPermisos(id), SuccessCode.ESTADO_CIVIL_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody EstadoCivilRequest request) {
        try {
            EstadoCivil creado = estadoCivilService.crear(request);
            Map<String, Object> data = Map.of("estadoCivil", creado);
            return success(data, SuccessCode.ESTADO_CIVIL_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody EstadoCivilRequest request) {
        try {
            EstadoCivil actualizado = estadoCivilService.actualizar(id, request);
            Map<String, Object> data = Map.of("estadoCivil", actualizado);
            return success(data, SuccessCode.ESTADO_CIVIL_UPDATED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            estadoCivilService.eliminarBasePermisos(id);
            return success(null, SuccessCode.ESTADO_CIVIL_DELETED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), ex.getCodigoNumerico() == 409 ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND);
        }
    }
}
