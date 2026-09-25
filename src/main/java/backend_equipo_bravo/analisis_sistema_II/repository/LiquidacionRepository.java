package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.Liquidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LiquidacionRepository extends JpaRepository<Liquidacion, Integer> {
    List<Liquidacion> findByIdEmpleado(Integer idEmpleado);

    Optional<Liquidacion> findByIdEmpleadoAndFechaContratacion(Integer idEmpleado, LocalDate fechaContratacion);
}