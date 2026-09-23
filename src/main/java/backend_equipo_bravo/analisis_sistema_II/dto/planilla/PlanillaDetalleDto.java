package backend_equipo_bravo.analisis_sistema_II.dto.planilla;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PlanillaDetalleDto {
    private Integer idPlanillaDetalle;
    private Integer idEmpleado;
    private String nombres;
    private LocalDate fechaContratacion;
    private BigDecimal ingresoSueldoBase;
    private BigDecimal ingresoBonificacionDecreto;
    private BigDecimal ingresoOtrosIngresos;
    private BigDecimal descuentoIgss;
    private BigDecimal descuentoIsr;
    private BigDecimal descuentoInasistencias;
    private BigDecimal salarioNeto;
}