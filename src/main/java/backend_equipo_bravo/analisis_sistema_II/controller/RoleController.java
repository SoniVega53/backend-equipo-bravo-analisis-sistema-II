package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.RoleRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Role;
import backend_equipo_bravo.analisis_sistema_II.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> listar() {

        return ResponseEntity.ok(
                roleService.buscarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> obtenerPorId(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                roleService.buscarPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<Role> crear(
            @RequestBody RoleRequest request
    ) {

        return ResponseEntity.ok(
                roleService.crear(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> actualizar(
            @PathVariable Integer id,
            @RequestBody RoleRequest request
    ) {

        return ResponseEntity.ok(
                roleService.actualizar(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id
    ) {

        roleService.eliminarBase(id);

        return ResponseEntity.noContent().build();
    }
}