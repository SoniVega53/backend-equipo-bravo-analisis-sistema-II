package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmpleadoRequest {
    private Integer idPersona;
    private Integer idSucursal;
    private LocalDate fechaContratacion;
    private Integer idPuesto;
    private Integer idStatusEmpleado;
    private BigDecimal ingresoSueldoBase;
    private BigDecimal ingresoBonificacionDecreto;
    private BigDecimal ingresoOtrosIngresos;
    private BigDecimal descuentoIgss;
    private BigDecimal descuentoIsr;
    private BigDecimal descuentoInasistencias;
}
