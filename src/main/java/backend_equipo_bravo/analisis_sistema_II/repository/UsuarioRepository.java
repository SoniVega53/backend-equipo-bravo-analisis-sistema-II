package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByIdUsuario(String idUsuario);

    @Query("""
                SELECT u.idSucursal
                FROM Usuario u
                WHERE u.idUsuario = :idUsuario
            """)
    Optional<Integer> findIdSucursalByIdUsuario(
            @Param("idUsuario") String idUsuario
    );
}