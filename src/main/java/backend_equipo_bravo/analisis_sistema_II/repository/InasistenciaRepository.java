package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.Inasistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InasistenciaRepository extends JpaRepository<Inasistencia, Integer> {
    List<Inasistencia> findByIdEmpleado(Integer idEmpleado);
}