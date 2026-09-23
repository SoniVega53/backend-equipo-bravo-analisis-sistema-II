package backend_equipo_bravo.analisis_sistema_II.dto.planilla;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanillaResponseDto {
    private Integer anio;
    private Integer mes;
    private BigDecimal totalIngresos;
    private BigDecimal totalDescuentos;
    private BigDecimal salarioNeto;
    private LocalDateTime fechaHoraProcesada;
    private List<PlanillaDetalleDto> detalles;
}