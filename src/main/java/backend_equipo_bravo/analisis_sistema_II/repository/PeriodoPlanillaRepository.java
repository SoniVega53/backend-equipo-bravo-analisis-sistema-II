package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.PeriodoPlanilla;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.PeriodoPlanillaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodoPlanillaRepository extends JpaRepository<PeriodoPlanilla, PeriodoPlanillaId> {
    Optional<PeriodoPlanilla> findByAnioAndMes(Integer anio, Integer mes);
}