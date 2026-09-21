package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idempleado")
    private Integer idEmpleado;

    @Column(name = "idpersona")
    private Integer idPersona;

    @Column(name = "idsucursal")
    private Integer idSucursal;

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

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}