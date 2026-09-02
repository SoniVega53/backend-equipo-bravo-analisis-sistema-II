package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.Data;

@Data
public class MenuRequest {
    private Integer idModulo;
    private String nombre;
    private Integer ordenMenu;
}