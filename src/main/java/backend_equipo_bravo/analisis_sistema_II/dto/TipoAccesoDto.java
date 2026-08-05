package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoAccesoDto {
    private Integer idTipoAcceso;
    private String nombre;
}