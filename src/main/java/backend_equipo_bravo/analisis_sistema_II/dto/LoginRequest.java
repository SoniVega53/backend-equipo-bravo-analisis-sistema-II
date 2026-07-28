package backend_equipo_bravo.analisis_sistema_II.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "El usuario es obligatorio")
    private String idUsuario;

    @NotBlank(message = "El password es obligatorio")
    private String password;
}