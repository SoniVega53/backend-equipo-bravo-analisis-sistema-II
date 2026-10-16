
package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.entity.FlujoStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.service.FlujoStatusEmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/flujo-status-empleado")
public class FlujoStatusEmpleadoController {

    private final FlujoStatusEmpleadoService service;

    public FlujoStatusEmpleadoController(
            FlujoStatusEmpleadoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{actual}/{nuevo}")
    public ResponseEntity<?> obtener(
            @PathVariable Integer actual,
            @PathVariable Integer nuevo) {

        FlujoStatusEmpleado flujo = service.obtener(actual, nuevo);

        if (flujo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "El flujo no existe"));
        }

        return ResponseEntity.ok(flujo);
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody FlujoStatusEmpleado flujo) {

        if (flujo.getIdStatusActual() == null ||
                flujo.getIdStatusNuevo() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje",
                            "Los dos estados son obligatorios"));
        }

        if (service.obtener(
                flujo.getIdStatusActual(),
                flujo.getIdStatusNuevo()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje",
                            "La transición ya existe"));
        }

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.guardar(flujo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PutMapping("/{actual}/{nuevo}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer actual,
            @PathVariable Integer nuevo,
            @RequestBody FlujoStatusEmpleado flujo) {

        if (!actual.equals(flujo.getIdStatusActual()) ||
                !nuevo.equals(flujo.getIdStatusNuevo())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje",
                            "Los estados del cuerpo deben coincidir con la URL"));
        }

        if (service.obtener(actual, nuevo) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "El flujo no existe"));
        }

        try {
            return ResponseEntity.ok(service.guardar(flujo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/{actual}/{nuevo}")
    public ResponseEntity<?> eliminar(
            @PathVariable Integer actual,
            @PathVariable Integer nuevo) {

        if (service.obtener(actual, nuevo) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "El flujo no existe"));
        }

        service.eliminar(actual, nuevo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validar")
    public ResponseEntity<?> validar(
            @RequestParam Integer actual,
            @RequestParam Integer nuevo) {

        if (actual.equals(nuevo)) {
            return ResponseEntity.ok(Map.of(
                    "permitida", false,
                    "mensaje", "El estado actual y el nuevo deben ser diferentes"
            ));
        }

        boolean permitida =
                service.transicionPermitida(actual, nuevo);

        return ResponseEntity.ok(Map.of(
                "permitida", permitida,
                "mensaje", permitida
                        ? "Transición permitida"
                        : "Transición no permitida"
        ));
    }
}
