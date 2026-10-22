package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.flujostatusempleado.FlujoStatusEmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.FlujoStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.FlujoStatusEmpleadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/flujo-status-empleado")
@Tag(name = "Flujo Status Empleado", description = "Endpoints para CRUD flujo status empleado")
public class FlujoStatusEmpleadoController extends BaseController {

    @Autowired
    private FlujoStatusEmpleadoService flujoStatusEmpleadoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(flujoStatusEmpleadoService.buscarTodosPermisos(), SuccessCode.FLUJO_STATUS_EMPLEADO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/detallado")
    public ResponseEntity<?> listarTodosDetallados() {
        try {
            return success(flujoStatusEmpleadoService.listarDetalladosPermisos(), SuccessCode.FLUJO_STATUS_EMPLEADO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/status-actual/{idStatusActual}")
    public ResponseEntity<?> listarPorStatusActual(@PathVariable Integer idStatusActual) {
        try {
            return success(flujoStatusEmpleadoService.buscarPorStatusActualPermisos(idStatusActual), SuccessCode.FLUJO_STATUS_EMPLEADO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{idStatusActual}/{idStatusNuevo}")
    public ResponseEntity<?> obtenerPorId(
            @PathVariable Integer idStatusActual,
            @PathVariable Integer idStatusNuevo) {
        try {
            FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(idStatusActual, idStatusNuevo);
            return success(flujoStatusEmpleadoService.buscarPorIdPermisos(id), SuccessCode.FLUJO_STATUS_EMPLEADO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody FlujoStatusEmpleadoRequest request) {
        try {
            FlujoStatusEmpleado creada = flujoStatusEmpleadoService.crear(request);
            Map<String, Object> data = Map.of("flujoStatusEmpleado", creada);
            return success(data, SuccessCode.FLUJO_STATUS_EMPLEADO_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{idStatusActual}/{idStatusNuevo}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer idStatusActual,
            @PathVariable Integer idStatusNuevo,
            @RequestBody FlujoStatusEmpleadoRequest request) {
        try {
            FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(idStatusActual, idStatusNuevo);
            FlujoStatusEmpleado actualizada = flujoStatusEmpleadoService.actualizar(id, request);
            Map<String, Object> data = Map.of("flujoStatusEmpleado", actualizada);
            return success(data, SuccessCode.FLUJO_STATUS_EMPLEADO_UPDATED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{idStatusActual}/{idStatusNuevo}")
    public ResponseEntity<?> eliminar(
            @PathVariable Integer idStatusActual,
            @PathVariable Integer idStatusNuevo) {
        try {
            FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(idStatusActual, idStatusNuevo);
            flujoStatusEmpleadoService.eliminarBasePermisos(id);
            return success(null, SuccessCode.FLUJO_STATUS_EMPLEADO_DELETED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(),
                    e.getCodigoNumerico() == 409 ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND);
        }
    }
}

