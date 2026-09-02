package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.Menu;
import backend_equipo_bravo.analisis_sistema_II.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    List<Sucursal> findByIdEmpresa(Integer idEmpresa);
}
