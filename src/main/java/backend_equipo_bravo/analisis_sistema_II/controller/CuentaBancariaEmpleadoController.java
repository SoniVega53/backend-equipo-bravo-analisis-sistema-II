
package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.cuentabancariaempleado.CuentaBancariaEmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.CuentaBancariaEmpleado;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.CuentaBancariaEmpleadoService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/cuentas-bancarias-empleados")
@Tag(
    name = "CuentaBancariaEmpleado",
    description = "Endpoints para CRUD de cuentas bancarias de empleados"
)
public class CuentaBancariaEmpleadoController extends BaseController {

    @Autowired
    private CuentaBancariaEmpleadoService cuentaBancariaEmpleadoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        return success(
            cuentaBancariaEmpleadoService.buscarTodosPermisos(),
            SuccessCode.CUENTA_BANCARIA_EMPLEADO_GENERAL
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(
                cuentaBancariaEmpleadoService.buscarPorIdPermisos(id),
                SuccessCode.CUENTA_BANCARIA_EMPLEADO_GENERAL
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
            @RequestBody CuentaBancariaEmpleadoRequest request) {
        try {
            CuentaBancariaEmpleado creada =
                cuentaBancariaEmpleadoService.crear(request);

            return success(
                Map.of("cuentaBancariaEmpleado", creada),
                SuccessCode.CUENTA_BANCARIA_EMPLEADO_SUCCESS
            );
        } catch (IllegalArgumentException e) {
            return error(
                400,
                "CUENTA_BANCARIA_EMPLEADO_INVALIDA",
                e.getMessage(),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody CuentaBancariaEmpleadoRequest request) {
        try {
            CuentaBancariaEmpleado actualizada =
                cuentaBancariaEmpleadoService.actualizar(id, request);

            return success(
                Map.of("cuentaBancariaEmpleado", actualizada),
                SuccessCode.CUENTA_BANCARIA_EMPLEADO_UPDATED_SUCCESS
            );
        } catch (BusinessException e) {
            return error(
                e.getCodigoNumerico(),
                e.getCodigoTexto(),
                e.getMessage(),
                HttpStatus.NOT_FOUND
            );
        } catch (IllegalArgumentException e) {
            return error(
                400,
                "CUENTA_BANCARIA_EMPLEADO_INVALIDA",
                e.getMessage(),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            cuentaBancariaEmpleadoService.eliminarBase(id);

            return success(
                null,
                SuccessCode.CUENTA_BANCARIA_EMPLEADO_DELETED_SUCCESS
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
