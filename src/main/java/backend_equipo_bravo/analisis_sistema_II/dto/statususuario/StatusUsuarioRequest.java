package backend_equipo_bravo.analisis_sistema_II.dto.statususuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StatusUsuarioRequest {

    @NotBlank(message = "El nombre del estatus es obligatorio")
    @Size(max = 50, message = "El nombre del estatus no puede superar los 50 caracteres")
    private String nombre;
}