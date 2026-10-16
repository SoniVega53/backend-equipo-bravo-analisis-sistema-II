package backend_equipo_bravo.analisis_sistema_II.dto.planilla;

import lombok.Data;

@Data
public class BoletaPagoRequestDto {
    private Integer anio;
    private Integer mes;
    private Integer idEmpleado;
}