package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;

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
    private LocalDateTime fechaInicial;

    @Column(name = "fechafinal")
    private LocalDateTime fechaFinal;

    @Column(name = "motivoinasistencia")
    private String motivoInasistencia;

    @Column(name = "fechaprocesado")
    private LocalDateTime fechaProcesado;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}