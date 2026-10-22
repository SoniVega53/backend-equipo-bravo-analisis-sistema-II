package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.tipodocumento.TipoDocumentoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.TipoDocumento;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.TipoDocumentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/tipo-documento")
@Tag(name = "TipoDocumento", description = "Endpoints para CRUD tipo de documento")
public class TipoDocumentoController extends BaseController {
    @Autowired
    private TipoDocumentoService tipoDocumentoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(tipoDocumentoService.buscarTodosPermisos(), SuccessCode.TIPO_DOCUMENTO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(tipoDocumentoService.buscarPorIdPermisos(id), SuccessCode.TIPO_DOCUMENTO_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody TipoDocumentoRequest request) {
        try {
            TipoDocumento creado = tipoDocumentoService.crear(request);
            Map<String, Object> data = Map.of("genero", creado);
            return success(data, SuccessCode.TIPO_DOCUMENTO_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody TipoDocumentoRequest request) {
        try {
            TipoDocumento actualizado = tipoDocumentoService.actualizar(id, request);
            Map<String, Object> data = Map.of("genero", actualizado);
            return success(data, SuccessCode.TIPO_DOCUMENTO_UPDATED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            tipoDocumentoService.eliminarBasePermisos(id);
            return success(null, SuccessCode.TIPO_DOCUMENTO_DELETED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}