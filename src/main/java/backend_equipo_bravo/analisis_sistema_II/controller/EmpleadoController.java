
package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
@RestController
@RequestMapping("/api/console/empleados")
@Tag(name = "Empleado", description = "Consulta de empleados")
public class EmpleadoController extends BaseController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        return success(
    empleadoService.listarEmpleados(),
    SuccessCode.EMPRESA_GENERAL
);
    }
}
