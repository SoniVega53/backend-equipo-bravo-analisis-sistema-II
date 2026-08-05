package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordPolicyDto {
    private String regex;
    private String mensajeValidacion;
    private Integer largoMinimo;
}