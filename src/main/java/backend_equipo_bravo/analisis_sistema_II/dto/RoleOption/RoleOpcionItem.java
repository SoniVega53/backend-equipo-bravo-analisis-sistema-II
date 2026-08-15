package backend_equipo_bravo.analisis_sistema_II.dto.RoleOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleOpcionItem {

    private Integer idRole;

    private Integer idOpcion;

    @Builder.Default
    private Integer consultar = 0;

    @Builder.Default
    private Integer alta = 0;

    @Builder.Default
    private Integer baja = 0;

    @Builder.Default
    private Integer cambio = 0;

    @Builder.Default
    private Integer imprimir = 0;

    @Builder.Default
    private Integer exportar = 0;
}
