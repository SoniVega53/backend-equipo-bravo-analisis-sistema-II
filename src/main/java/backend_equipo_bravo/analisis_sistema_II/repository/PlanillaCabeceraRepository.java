package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.PlanillaCabecera;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.PeriodoPlanillaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanillaCabeceraRepository extends JpaRepository<PlanillaCabecera, PeriodoPlanillaId> {
    Optional<PlanillaCabecera> findByAnioAndMes(Integer anio, Integer mes);
    void deleteByAnioAndMes(Integer anio, Integer mes);
}