package backend_equipo_bravo.analisis_sistema_II.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "http://localhost:4200")
public class EmpresaController {

    @GetMapping
    public List<Map<String, Object>> listarTodas() {
        return List.of(
            Map.of("idEmpresa", 1, "nombre", "Software Inc.")
        );
    }
}