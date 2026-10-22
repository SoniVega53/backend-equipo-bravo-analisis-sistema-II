package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.EmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Empleado;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.EmpleadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/empleado")
@Tag(name = "Empleado", description = "Endpoints para CRUD empleado")
public class EmpleadoController extends BaseController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(empleadoService.buscarTodosPermisos(), SuccessCode.EMPLEADO_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(empleadoService.buscarPorIdPermisos(id), SuccessCode.EMPLEADO_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/status-permitidos")
    public ResponseEntity<?> statusPermitidos(@PathVariable Integer id) {
        try {
            return success(empleadoService.statusPermitidos(id), SuccessCode.EMPLEADO_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody EmpleadoRequest request) {
        try {
            Empleado creado = empleadoService.crear(request);
            Map<String, Object> data = Map.of("empleado", creado);
            return success(data, SuccessCode.EMPLEADO_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody EmpleadoRequest request) {
        try {
            Empleado actualizado = empleadoService.actualizar(id, request);
            Map<String, Object> data = Map.of("empleado", actualizado);
            return success(data, SuccessCode.EMPLEADO_UPDATED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            empleadoService.eliminarBasePermisos(id);
            return success(null, SuccessCode.EMPLEADO_DELETED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), ex.getCodigoNumerico() == 409 ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND);
        }
    }
}
