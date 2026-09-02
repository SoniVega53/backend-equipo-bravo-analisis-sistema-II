package backend_equipo_bravo.analisis_sistema_II.dto.usuario;

import lombok.Data;

@Data
public class ValidarRespuestaRequest {
    private String idUsuario;
    private String respuesta;
}