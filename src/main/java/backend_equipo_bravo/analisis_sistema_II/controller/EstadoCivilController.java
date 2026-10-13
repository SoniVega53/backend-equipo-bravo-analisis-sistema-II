package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.entity.EstadoCivil;
import backend_equipo_bravo.analisis_sistema_II.service.EstadoCivilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-civiles")
@CrossOrigin(origins = "*")
public class EstadoCivilController {

    @Autowired
    private EstadoCivilService estadoCivilService;

    @GetMapping
    public List<EstadoCivil> listar() {
        return estadoCivilService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoCivil> obtenerPorId(@PathVariable Integer id) {
        return estadoCivilService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EstadoCivil crear(@RequestBody EstadoCivil estadoCivil) {
        return estadoCivilService.guardar(estadoCivil);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoCivil> actualizar(@PathVariable Integer id, @RequestBody EstadoCivil estadoCivil) {
        return estadoCivilService.obtenerPorId(id)
                .map(existente -> {
                    estadoCivil.setIdEstadoCivil(id);
                    return ResponseEntity.ok(estadoCivilService.guardar(estadoCivil));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (estadoCivilService.obtenerPorId(id).isPresent()) {
            estadoCivilService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}