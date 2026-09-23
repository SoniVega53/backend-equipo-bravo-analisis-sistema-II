package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.PlanillaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanillaDetalleRepository extends JpaRepository<PlanillaDetalle, Integer> {
    List<PlanillaDetalle> findByAnioAndMes(Integer anio, Integer mes);
    void deleteByAnioAndMes(Integer anio, Integer mes);
}