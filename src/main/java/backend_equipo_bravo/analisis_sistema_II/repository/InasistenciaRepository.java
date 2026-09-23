package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.Inasistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InasistenciaRepository extends JpaRepository<Inasistencia, Integer> {
    List<Inasistencia> findByIdEmpleado(Integer idEmpleado);
    List<Inasistencia> findByIdEmpleadoAndFechaProcesadoIsNull(Integer idEmpleado);


    @Query("SELECT COUNT(i) > 0 FROM Inasistencia i " +
            "WHERE i.idEmpleado = :idEmpleado " +
            "AND :fechaInicio <= i.fechaFinal " +
            "AND :fechaFin >= i.fechaInicial")
    boolean existeSolapamiento(
            @Param("idEmpleado") Integer idEmpleado,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    @Query("SELECT COUNT(i) > 0 FROM Inasistencia i " +
            "WHERE i.idEmpleado = :idEmpleado " +
            "AND i.idInasistencia <> :idInasistencia " +
            "AND :fechaInicio <= i.fechaFinal " +
            "AND :fechaFin >= i.fechaInicial")
    boolean existeSolapamientoParaActualizar(
            @Param("idEmpleado") Integer idEmpleado,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("idInasistencia") Integer idInasistencia
    );
}