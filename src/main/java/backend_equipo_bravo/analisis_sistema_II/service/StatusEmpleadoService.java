
package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.statusempleado.StatusEmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.StatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.repository.StatusEmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StatusEmpleadoService extends BaseService<StatusEmpleado, Integer> {

    @Autowired
    private StatusEmpleadoRepository statusEmpleadoRepository;

    @Override
    protected JpaRepository<StatusEmpleado, Integer> getRepository() {
        return statusEmpleadoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new RuntimeException("No se encontró el estatus de empleado con ID: " + id);
    }

    public StatusEmpleado crear(StatusEmpleadoRequest request) {
        String usuarioActual = obtenerUsuarioAutenticado();

        StatusEmpleado statusEmpleado = new StatusEmpleado();
        statusEmpleado.setNombre(request.getNombre());
        statusEmpleado.setUsuarioCreacion(usuarioActual);
        statusEmpleado.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(statusEmpleado);
    }

    public StatusEmpleado actualizar(Integer id, StatusEmpleadoRequest request) {
        StatusEmpleado existente = super.buscarPorId(id);

        String usuarioActual = obtenerUsuarioAutenticado();

        existente.setNombre(request.getNombre());
        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }
}
