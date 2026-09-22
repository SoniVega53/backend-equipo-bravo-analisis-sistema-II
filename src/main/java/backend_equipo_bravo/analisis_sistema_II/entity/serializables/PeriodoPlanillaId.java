package backend_equipo_bravo.analisis_sistema_II.entity.serializables;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoPlanillaId implements Serializable {
    private Integer anio;
    private Integer mes;
}