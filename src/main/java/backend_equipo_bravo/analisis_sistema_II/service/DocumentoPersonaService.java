
package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.documentopersona.DocumentoPersonaRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.DocumentoPersona;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.DocumentoPersonaId;
import backend_equipo_bravo.analisis_sistema_II.repository.DocumentoPersonaRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.PersonaRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.TipoDocumentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DocumentoPersonaService
        extends BaseService<DocumentoPersona, DocumentoPersonaId> {

    @Autowired
    private DocumentoPersonaRepository documentoPersonaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Override
    protected JpaRepository<DocumentoPersona, DocumentoPersonaId>
    getRepository() {
        return documentoPersonaRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(DocumentoPersonaId id) {
        return new RuntimeException(
                "No se encontró el documento de la persona indicada."
        );
    }

    public DocumentoPersona crear(DocumentoPersonaRequest request) {
        validarRequest(request);
        validarReferencias(request);

        if (documentoPersonaRepository.existsByIdTipoDocumentoAndIdPersona(
                request.getIdTipoDocumento(),
                request.getIdPersona())) {
            throw new IllegalArgumentException(
                    "Esta persona ya tiene registrado un documento de ese tipo."
            );
        }

        DocumentoPersona documento = new DocumentoPersona();

        documento.setIdTipoDocumento(request.getIdTipoDocumento());
        documento.setIdPersona(request.getIdPersona());
        documento.setNoDocumento(limpiarNumero(request.getNoDocumento()));
        documento.setFechaCreacion(LocalDateTime.now());
        documento.setUsuarioCreacion(obtenerUsuarioAutenticado());

        return super.crearBasePermisos(documento);
    }

    public DocumentoPersona actualizar(
            DocumentoPersonaId id,
            DocumentoPersonaRequest request) {

        validarRequest(request);
        validarReferencias(request);

        if (!id.getIdTipoDocumento().equals(request.getIdTipoDocumento())
                || !id.getIdPersona().equals(request.getIdPersona())) {
            throw new IllegalArgumentException(
                    "No se puede cambiar la persona ni el tipo de documento "
                    + "de un registro existente."
            );
        }

        DocumentoPersona existente = super.buscarPorId(id);

        existente.setNoDocumento(limpiarNumero(request.getNoDocumento()));
        existente.setFechaModificacion(LocalDateTime.now());
        existente.setUsuarioModificacion(obtenerUsuarioAutenticado());

        return super.actualizarBasePermisos(existente);
    }

    public String eliminar(DocumentoPersonaId id) {
        super.eliminarBasePermisos(id);
        return "Documento eliminado correctamente.";
    }

    private void validarRequest(DocumentoPersonaRequest request) {
        if (request == null
                || request.getIdPersona() == null
                || request.getIdTipoDocumento() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una persona y un tipo de documento."
            );
        }
    }

    private void validarReferencias(DocumentoPersonaRequest request) {
        if (!personaRepository.existsById(request.getIdPersona())) {
            throw new IllegalArgumentException(
                    "La persona seleccionada no existe."
            );
        }

        if (!tipoDocumentoRepository.existsById(
                request.getIdTipoDocumento())) {
            throw new IllegalArgumentException(
                    "El tipo de documento seleccionado no existe."
            );
        }
    }

    private String limpiarNumero(String numero) {
        return numero == null || numero.isBlank()
                ? null
                : numero.trim();
    }
}
