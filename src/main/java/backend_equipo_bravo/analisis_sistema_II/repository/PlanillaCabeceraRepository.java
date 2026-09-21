package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.PlanillaCabecera;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.PeriodoPlanillaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanillaCabeceraRepository extends JpaRepository<PlanillaCabecera, PeriodoPlanillaId> {
}