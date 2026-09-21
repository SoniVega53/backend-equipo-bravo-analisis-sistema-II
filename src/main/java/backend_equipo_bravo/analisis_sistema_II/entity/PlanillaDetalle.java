package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "planilla_detalle")
public class PlanillaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idplanilladetalle")
    private Integer idPlanillaDetalle;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "idempleado")
    private Integer idEmpleado;

    @Column(name = "fechacontratacion")
    private LocalDate fechaContratacion;

    @Column(name = "idpuesto")
    private Integer idPuesto;

    @Column(name = "idstatusempleado")
    private Integer idStatusEmpleado;

    @Column(name = "ingresosueldobase")
    private BigDecimal ingresoSueldoBase;

    @Column(name = "ingresobonificaciondecreto")
    private BigDecimal ingresoBonificacionDecreto;

    @Column(name = "ingresootrosingresos")
    private BigDecimal ingresoOtrosIngresos;

    @Column(name = "descuentoigss")
    private BigDecimal descuentoIgss;

    @Column(name = "descuentoisr")
    private BigDecimal descuentoIsr;

    @Column(name = "descuentoinasistencias")
    private BigDecimal descuentoInasistencias;

    @Column(name = "salarioneto")
    private BigDecimal salarioNeto;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}