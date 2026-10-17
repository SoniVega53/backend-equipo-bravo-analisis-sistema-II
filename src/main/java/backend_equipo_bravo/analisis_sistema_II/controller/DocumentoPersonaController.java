
package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.documentopersona.DocumentoPersonaRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.DocumentoPersona;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.DocumentoPersonaId;
import backend_equipo_bravo.analisis_sistema_II.service.DocumentoPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/console/documentos-personas")
public class DocumentoPersonaController {

    @Autowired
    private DocumentoPersonaService documentoPersonaService;

    @GetMapping
    public List<DocumentoPersona> listarTodos() {
        return documentoPersonaService.buscarTodosPermisos();
    }

    @GetMapping("/{idTipoDocumento}/{idPersona}")
    public DocumentoPersona buscarPorId(
            @PathVariable Integer idTipoDocumento,
            @PathVariable Integer idPersona) {

        DocumentoPersonaId id =
                new DocumentoPersonaId(idTipoDocumento, idPersona);

        return documentoPersonaService.buscarPorIdPermisos(id);
    }

    @PostMapping
    public DocumentoPersona crear(
            @RequestBody DocumentoPersonaRequest request) {

        return documentoPersonaService.crear(request);
    }

    @PutMapping("/{idTipoDocumento}/{idPersona}")
    public DocumentoPersona actualizar(
            @PathVariable Integer idTipoDocumento,
            @PathVariable Integer idPersona,
            @RequestBody DocumentoPersonaRequest request) {

        DocumentoPersonaId id =
                new DocumentoPersonaId(idTipoDocumento, idPersona);

        return documentoPersonaService.actualizar(id, request);
    }

    @DeleteMapping("/{idTipoDocumento}/{idPersona}")
    public String eliminar(
            @PathVariable Integer idTipoDocumento,
            @PathVariable Integer idPersona) {

        DocumentoPersonaId id =
                new DocumentoPersonaId(idTipoDocumento, idPersona);

        return documentoPersonaService.eliminar(id);
    }
}
