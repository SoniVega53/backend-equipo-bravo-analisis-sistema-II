package backend_equipo_bravo.analisis_sistema_II.entity;

import backend_equipo_bravo.analisis_sistema_II.entity.serializables.DocumentoPersonaId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "documento_persona")
@IdClass(DocumentoPersonaId.class)
public class DocumentoPersona {

    @Id
    @Column(name = "idtipodocumento")
    private Integer idTipoDocumento;

    @Id
    @Column(name = "idpersona")
    private Integer idPersona;

    @Column(name = "nodocumento")
    private String noDocumento;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}