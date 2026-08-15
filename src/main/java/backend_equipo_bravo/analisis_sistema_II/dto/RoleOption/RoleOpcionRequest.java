package backend_equipo_bravo.analisis_sistema_II.dto.RoleOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleOpcionRequest {
    private List<RoleOpcionItem> roleOpcionItems;
}
