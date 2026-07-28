package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private String codigo;
    private String mensaje;
}