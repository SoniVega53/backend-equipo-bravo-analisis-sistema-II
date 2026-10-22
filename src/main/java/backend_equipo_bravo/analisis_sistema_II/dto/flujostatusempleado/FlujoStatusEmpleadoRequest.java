package backend_equipo_bravo.analisis_sistema_II.dto.flujostatusempleado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlujoStatusEmpleadoRequest {

    @NotNull(message = "El status actual es obligatorio")
    private Integer idStatusActual;

    @NotNull(message = "El status nuevo es obligatorio")
    private Integer idStatusNuevo;

    @NotBlank(message = "El nombre del evento que origina el cambio es obligatorio")
    @Size(max = 100, message = "El nombre del evento no puede superar los 100 caracteres")
    private String nombreEvento;
}

