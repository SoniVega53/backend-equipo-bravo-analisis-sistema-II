package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {

    private String id;

    private String label;

    private String url;

    private List<MenuItemDto> children;

    @Builder.Default
    private Boolean expanded = false;
}