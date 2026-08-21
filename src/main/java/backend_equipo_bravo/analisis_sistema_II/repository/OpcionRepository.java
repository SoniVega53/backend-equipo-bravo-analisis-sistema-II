package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Integer> {
    Optional<Opcion> findByPagina(String pagina);
    Optional<Opcion> findByIdOpcion(Integer idOpcion);
    List<Opcion> findByIdMenuIn(List<Integer> idsMenu);
    List<Opcion> findByIdMenuInAndIdOpcionNot(List<Integer> idsMenu,Integer idOpcionExcluido);

}