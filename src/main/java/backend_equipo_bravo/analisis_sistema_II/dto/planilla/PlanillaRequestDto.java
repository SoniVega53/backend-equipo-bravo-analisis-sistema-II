package backend_equipo_bravo.analisis_sistema_II.dto.planilla;

import lombok.Data;

@Data
public class PlanillaRequestDto {
    private Integer anio;
    private Integer mes;
    private Boolean forzarRecalculo;
}