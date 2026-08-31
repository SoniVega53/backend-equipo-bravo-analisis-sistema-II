package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.empresa.EmpresaRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Empresa;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.EmpresaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/empresa")
@Tag(name = "Empresa", description = "Endpoints para CRUD de empresas")
public class EmpresaController extends BaseController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {

        try {

            return success(
                    empresaService.buscarTodos(),
                    SuccessCode.EMPRESA_GENERAL
            );

        } catch (BusinessException e) {

            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {

        try {

            return success(
                    empresaService.buscarPorId(id),
                    SuccessCode.EMPRESA_GENERAL
            );

        } catch (BusinessException e) {

            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody EmpresaRequest request) {

        try {

            Empresa creada = empresaService.crear(request);

            Map<String, Object> data = Map.of(
                    "empresa",
                    creada
            );

            return success(
                    data,
                    SuccessCode.EMPRESA_SUCCESS
            );

        } catch (BusinessException e) {

            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody EmpresaRequest request) {

        try {

            Empresa actualizada =
                    empresaService.actualizar(id, request);

            Map<String, Object> data = Map.of(
                    "empresa",
                    actualizada
            );

            return success(
                    data,
                    SuccessCode.EMPRESA_UPDATED_SUCCESS
            );

        } catch (BusinessException e) {

            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Integer id) {

        try {

            empresaService.eliminarBase(id);

            return success(
                    null,
                    SuccessCode.EMPRESA_DELETED_SUCCESS
            );

        } catch (BusinessException e) {

            return error(
                    e.getCodigoNumerico(),
                    e.getCodigoTexto(),
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}