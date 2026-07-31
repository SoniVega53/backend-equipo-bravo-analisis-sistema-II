package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleOptionDto {
    @Builder.Default
    private Boolean consultar = false;

    @Builder.Default
    private Boolean alta = false;

    @Builder.Default
    private Boolean baja = false;

    @Builder.Default
    private Boolean cambio = false;

    @Builder.Default
    private Boolean imprimir = false;

    @Builder.Default
    private Boolean exportar = false;
}
