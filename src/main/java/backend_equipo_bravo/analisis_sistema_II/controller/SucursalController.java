package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.entity.Sucursal;
import backend_equipo_bravo.analisis_sistema_II.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@CrossOrigin(origins = "*")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public List<Sucursal> listarTodas() {
        return sucursalService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerPorId(@PathVariable Integer id) {
        return sucursalService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sucursal crear(@RequestBody Sucursal sucursal) {
        return sucursalService.guardar(sucursal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizar(@PathVariable Integer id, @RequestBody Sucursal sucursal) {
        try {
            return ResponseEntity.ok(sucursalService.actualizar(id, sucursal));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            sucursalService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}