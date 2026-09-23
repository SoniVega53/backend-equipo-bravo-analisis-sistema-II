package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ReporteGenericoRequestDto {
    private String titulo;
    private List<ColumnaDto> columnas;
    private List<Map<String, Object>> datos;

    @Data
    public static class ColumnaDto {
        private String header;
        private String field;
    }
}