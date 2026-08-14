package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Integer> {
}