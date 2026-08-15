package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.dto.RoleOption.RoleOpcionId;
import backend_equipo_bravo.analisis_sistema_II.entity.RoleOpcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleOpcionRepository extends JpaRepository<RoleOpcion, RoleOpcionId> {
    List<RoleOpcion> findByIdRole(Integer idRole);

    Optional<RoleOpcion> findByIdRoleAndIdOpcion(Integer idRole, Integer idOpcion);
}