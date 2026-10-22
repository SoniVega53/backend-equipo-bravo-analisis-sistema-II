package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.FlujoStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlujoStatusEmpleadoRepository extends JpaRepository<FlujoStatusEmpleado, FlujoStatusEmpleadoId> {

    List<FlujoStatusEmpleado> findByIdStatusActual(Integer idStatusActual);

    List<FlujoStatusEmpleado> findByIdStatusNuevo(Integer idStatusNuevo);

    boolean existsByIdStatusActualAndIdStatusNuevo(Integer idStatusActual, Integer idStatusNuevo);
}
