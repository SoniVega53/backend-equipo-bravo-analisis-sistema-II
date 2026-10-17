
package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.Empleado;
import backend_equipo_bravo.analisis_sistema_II.entity.Persona;
import backend_equipo_bravo.analisis_sistema_II.repository.EmpleadoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public List<Map<String, Object>> listarEmpleados() {
        return empleadoRepository.findAll()
            .stream()
            .map(empleado -> {
                Persona persona = personaRepository
                    .findById(empleado.getIdPersona())
                    .orElse(null);

                String nombreCompleto = persona == null
                    ? "Empleado #" + empleado.getIdEmpleado()
                    : persona.getNombre() + " " + persona.getApellido();

                return Map.<String, Object>of(
                    "idEmpleado", empleado.getIdEmpleado(),
                    "nombreCompleto", nombreCompleto
                );
            })
            .toList();
    }
}
