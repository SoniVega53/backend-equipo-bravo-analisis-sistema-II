package backend_equipo_bravo.analisis_sistema_II.dto.usuario;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioUpdatePasswordRequest {
    private String password;
}
