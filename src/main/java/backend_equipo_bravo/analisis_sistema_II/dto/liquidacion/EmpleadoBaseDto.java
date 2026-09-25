package backend_equipo_bravo.analisis_sistema_II.dto.liquidacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoBaseDto {
    private Integer idEmpleado;
    private Integer idPersona;
    private String nombreEmpleado;
    private Integer idPuesto;
    private String nombrePuesto;
    private Integer idDepartamento;
    private String nombreDepartamento;
    private Integer idSucursal;
    private Integer idStatusEmpleado;
    private String nombreStatus;
    private LocalDate fechaContratacion;
    private BigDecimal ingresoSueldoBase;
    private BigDecimal ingresoBonificacionDecreto;
    private BigDecimal ingresoOtrosIngresos;
    private BigDecimal descuentoIgss;
    private BigDecimal descuentoIsr;
    private BigDecimal descuentoInasistencias;
}
