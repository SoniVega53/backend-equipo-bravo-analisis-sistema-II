package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.documentopersona.DocumentoPersonaRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.DocumentoPersonaId;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.DocumentoPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/console/documentos-personas")
public class DocumentoPersonaController extends BaseController {

    @Autowired
    private DocumentoPersonaService documentoPersonaService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(documentoPersonaService.buscarTodosPermisos(), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{idTipoDocumento}/{idPersona}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable Integer idTipoDocumento,
            @PathVariable Integer idPersona) {
        try {
            DocumentoPersonaId id = new DocumentoPersonaId(idTipoDocumento, idPersona);
            return success(documentoPersonaService.buscarPorIdPermisos(id), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody DocumentoPersonaRequest request) {
        try {
            return success(documentoPersonaService.crear(request), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{idTipoDocumento}/{idPersona}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer idTipoDocumento,
            @PathVariable Integer idPersona,
            @RequestBody DocumentoPersonaRequest request) {
        try {
            DocumentoPersonaId id = new DocumentoPersonaId(idTipoDocumento, idPersona);
            return success(documentoPersonaService.actualizar(id, request), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{idTipoDocumento}/{idPersona}")
    public ResponseEntity<?> eliminar(
            @PathVariable Integer idTipoDocumento,
            @PathVariable Integer idPersona) {
        try {
            DocumentoPersonaId id = new DocumentoPersonaId(idTipoDocumento, idPersona);
            return success(documentoPersonaService.eliminarBasePermisos(id), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
