package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.FlujoStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import backend_equipo_bravo.analisis_sistema_II.repository.FlujoStatusEmpleadoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlujoStatusEmpleadoService {

    private final FlujoStatusEmpleadoRepository repository;

    public FlujoStatusEmpleadoService(FlujoStatusEmpleadoRepository repository) {
        this.repository = repository;
    }

    public List<FlujoStatusEmpleado> listar() {
        return repository.findAll();
    }

    public FlujoStatusEmpleado obtener(Integer actual, Integer nuevo) {
        return repository.findById(
            new FlujoStatusEmpleadoId(actual, nuevo)
        ).orElse(null);
    }

    public FlujoStatusEmpleado guardar(FlujoStatusEmpleado flujo) {
        if (flujo.getIdStatusActual() == null ||
            flujo.getIdStatusNuevo() == null) {
            throw new IllegalArgumentException(
                "Los estados actual y nuevo son obligatorios"
            );
        }

        if (flujo.getIdStatusActual().equals(flujo.getIdStatusNuevo())) {
            throw new IllegalArgumentException(
                "El estado actual y el nuevo deben ser diferentes"
            );
        }

        FlujoStatusEmpleado existente =
            obtener(flujo.getIdStatusActual(), flujo.getIdStatusNuevo());
        LocalDateTime ahora = LocalDateTime.now();

        if (existente == null) {
            if (flujo.getFechaCreacion() == null) {
                flujo.setFechaCreacion(ahora);
            }
            if (flujo.getUsuarioCreacion() == null || flujo.getUsuarioCreacion().isBlank()) {
                flujo.setUsuarioCreacion("system");
            }
        } else {
            flujo.setFechaCreacion(existente.getFechaCreacion());
            flujo.setUsuarioCreacion(existente.getUsuarioCreacion());
            flujo.setFechaModificacion(ahora);
            if (flujo.getUsuarioModificacion() == null || flujo.getUsuarioModificacion().isBlank()) {
                flujo.setUsuarioModificacion("system");
            }
        }

        try {
            return repository.saveAndFlush(flujo);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("El status actual o el nuevo no existe");
        }
    }

    public void eliminar(Integer actual, Integer nuevo) {
        repository.deleteById(
            new FlujoStatusEmpleadoId(actual, nuevo)
        );
    }

    public boolean transicionPermitida(Integer actual, Integer nuevo) {
        return repository.existsById(
            new FlujoStatusEmpleadoId(actual, nuevo)
        );
    }
}
