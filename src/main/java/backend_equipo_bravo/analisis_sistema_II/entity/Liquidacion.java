package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "liquidacion")
public class Liquidacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idliquidacion")
    private Integer idLiquidacion;

    @Column(name = "idempleado")
    private Integer idEmpleado;

    @Column(name = "fechacontratacion")
    private LocalDate fechaContratacion;

    @Column(name = "fechaegreso")
    private LocalDate fechaEgreso;

    @Column(name = "fechaliquidacion")
    private LocalDate fechaLiquidacion;

    @Column(name = "motivoegreso")
    private String motivoEgreso;

    @Column(name = "idpuesto")
    private Integer idPuesto;

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

    @Column(name = "totalingresos")
    private BigDecimal totalIngresos;

    @Column(name = "totaldescuentos")
    private BigDecimal totalDescuentos;

    @Column(name = "totalneto")
    private BigDecimal totalNeto;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

}