package backend_equipo_bravo.analisis_sistema_II.entity;

import backend_equipo_bravo.analisis_sistema_II.entity.serializables.PeriodoPlanillaId;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "planilla_cabecera")
@IdClass(PeriodoPlanillaId.class)
public class PlanillaCabecera {

    @Id
    @Column(name = "anio")
    private Integer anio;

    @Id
    @Column(name = "mes")
    private Integer mes;

    @Column(name = "totalingresos")
    private BigDecimal totalIngresos;

    @Column(name = "totaldescuentos")
    private BigDecimal totalDescuentos;

    @Column(name = "salarioneto")
    private BigDecimal salarioNeto;

    @Column(name = "fechahoraprocesada")
    private LocalDateTime fechaHoraProcesada;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}