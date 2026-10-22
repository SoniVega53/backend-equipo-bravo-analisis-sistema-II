package backend_equipo_bravo.analisis_sistema_II.dto.flujostatusempleado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlujoStatusEmpleadoResponseDto {

    private Integer idStatusActual;
    private String nombreStatusActual;
    private Integer idStatusNuevo;
    private String nombreStatusNuevo;
    private String nombreEvento;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
}

