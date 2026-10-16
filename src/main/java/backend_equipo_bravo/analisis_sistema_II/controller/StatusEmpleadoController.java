
package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.statusempleado.StatusEmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.StatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.StatusEmpleadoService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/status-empleado")
@Tag(name = "StatusEmpleado", description = "Endpoints para CRUD de estatus de empleado")
public class StatusEmpleadoController extends BaseController {

    @Autowired
    private StatusEmpleadoService statusEmpleadoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        return success(
                statusEmpleadoService.buscarTodosPermisos(),
                SuccessCode.STATUS_EMPLEADO_GENERAL
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(
                    statusEmpleadoService.buscarPorIdPermisos(id),
                    SuccessCode.STATUS_EMPLEADO_GENERAL
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
            @RequestBody StatusEmpleadoRequest statusEmpleadoRequest) {

        StatusEmpleado creado =
                statusEmpleadoService.crear(statusEmpleadoRequest);

        Map<String, Object> data = Map.of(
                "statusEmpleado", creado
        );

        return success(
                data,
                SuccessCode.STATUS_EMPLEADO_SUCCESS
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody StatusEmpleadoRequest statusEmpleadoRequest) {

        try {
            StatusEmpleado actualizado =
                    statusEmpleadoService.actualizar(
                            id,
                            statusEmpleadoRequest
                    );

            Map<String, Object> data = Map.of(
                    "statusEmpleado", actualizado
            );

            return success(
                    data,
                    SuccessCode.STATUS_EMPLEADO_UPDATED_SUCCESS
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
            statusEmpleadoService.eliminarBase(id);

            return success(
                    null,
                    SuccessCode.STATUS_EMPLEADO_DELETED_SUCCESS
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
