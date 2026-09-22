package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inasistencia")
public class Inasistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idinasistencia")
    private Integer idInasistencia;

    @Column(name = "idempleado")
    private Integer idEmpleado;

    @Column(name = "fechainicial")
    private LocalDate fechaInicial;

    @Column(name = "fechafinal")
    private LocalDate fechaFinal;

    @Column(name = "motivoinasistencia")
    private String motivoInasistencia;

    @Column(name = "fechaprocesado")
    private LocalDate fechaProcesado;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}