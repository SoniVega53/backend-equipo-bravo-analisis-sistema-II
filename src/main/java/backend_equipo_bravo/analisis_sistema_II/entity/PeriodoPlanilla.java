package backend_equipo_bravo.analisis_sistema_II.entity;

import backend_equipo_bravo.analisis_sistema_II.entity.serializables.PeriodoPlanillaId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "periodo_planilla")
@IdClass(PeriodoPlanillaId.class)
public class PeriodoPlanilla {

    @Id
    @Column(name = "anio")
    private Integer anio;

    @Id
    @Column(name = "mes")
    private Integer mes;

    @Column(name = "fechainicio")
    private LocalDate fechaInicio;

    @Column(name = "fechafin")
    private LocalDate fechaFin;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}