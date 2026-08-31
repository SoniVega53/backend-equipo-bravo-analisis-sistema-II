package backend_equipo_bravo.analisis_sistema_II.repository;


import backend_equipo_bravo.analisis_sistema_II.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    Optional<Empresa> findByNit(String nit);
    Optional<Empresa> findByIdEmpresa(Integer id);
    boolean existsByNit(String nit);
}