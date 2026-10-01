package backend_equipo_bravo.analisis_sistema_II.dto.periodo_planilla;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PeriodoPlanillaDto {
    private Integer anio;
    private Integer mes;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
}
