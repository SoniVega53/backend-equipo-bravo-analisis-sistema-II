package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.entity.StatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.repository.StatusEmpleadoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/console/status-empleado")
public class StatusEmpleadoController {

    private final StatusEmpleadoRepository repository;

    public StatusEmpleadoController(StatusEmpleadoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<StatusEmpleado> listar() {
        return repository.findAll();
    }
}
