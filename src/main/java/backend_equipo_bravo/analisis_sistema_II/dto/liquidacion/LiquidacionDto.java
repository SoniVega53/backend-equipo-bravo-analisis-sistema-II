package backend_equipo_bravo.analisis_sistema_II.dto.liquidacion;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LiquidacionDto {
    private Integer idLiquidacion;
    private Integer idEmpleado;
    private String nombreEmpleado;
    private LocalDate fechaContratacion;
    private LocalDate fechaEgreso;
    private LocalDate fechaLiquidacion;
    private String motivoEgreso;
    private Integer idPuesto;
    private String nombrePuesto;
    private Integer idDepartamento;
    private String nombreDepartamento;
    private Integer idStatusEmpleado;
    private String nombreStatus;

    // Campos de la base de datos
    private BigDecimal ingresoSueldoBase;
    private BigDecimal ingresoBonificacionDecreto;
    private BigDecimal ingresoOtrosIngresos;
    private BigDecimal descuentoIgss;
    private BigDecimal descuentoIsr;
    private BigDecimal descuentoInasistencias;
    private BigDecimal salarioNeto;
    private BigDecimal totalIngresos;
    private BigDecimal totalDescuentos;
    private BigDecimal totalNeto;
    private LocalDateTime fechaCreacion;

    // Campos calculados en tiempo de ejecución (Desglose Guatemala)
    private Integer diasLaboradosTotal;
    private BigDecimal montoIndemnizacion;
    private Integer diasProporcionalesAguinaldo;
    private BigDecimal montoAguinaldo;
    private Integer diasProporcionalesBono14;
    private BigDecimal montoBono14;
    private Integer diasProporcionalesVacaciones;
    private BigDecimal montoVacaciones;
    private Integer diasPendientesPago;
    private BigDecimal montoSalarioPendiente;
    private BigDecimal montoBonificacionDecretoPendiente;
}