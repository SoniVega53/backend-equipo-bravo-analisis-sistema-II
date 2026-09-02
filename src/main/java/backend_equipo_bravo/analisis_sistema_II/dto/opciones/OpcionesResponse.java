package backend_equipo_bravo.analisis_sistema_II.dto.opciones;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OpcionesResponse {


    private Integer idOpcion;

    private Integer idMenu;
    private Integer idModulo;

    private String nombre;

    private Integer ordenMenu;

    private String pagina;

    private LocalDateTime fechaCreacion;

    private String usuarioCreacion;

    private LocalDateTime fechaModificacion;

    private String usuarioModificacion;
}
