package backend_equipo_bravo.analisis_sistema_II.dto.usuario;

import lombok.Data;

@Data
public class RecuperarPasswordUpdateRequest {

    private String idUsuario;
    private String password;
    private String respuesta;

}