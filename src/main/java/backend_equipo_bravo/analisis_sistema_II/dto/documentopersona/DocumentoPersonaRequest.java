package backend_equipo_bravo.analisis_sistema_II.dto.documentopersona;

import lombok.Data;

@Data
public class DocumentoPersonaRequest {

    private Integer idTipoDocumento;
    private Integer idPersona;
    private String noDocumento;
}
