package backend_equipo_bravo.analisis_sistema_II.dto.opciones;

import lombok.Data;

@Data
public class OpcionRequest {
    private Integer idMenu;
    private String nombre;
    private Integer ordenMenu;
    private String pagina;
}