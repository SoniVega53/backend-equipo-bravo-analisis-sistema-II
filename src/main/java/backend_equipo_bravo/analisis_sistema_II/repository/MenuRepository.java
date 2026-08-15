package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByIdModulo(Integer idModulo);
}