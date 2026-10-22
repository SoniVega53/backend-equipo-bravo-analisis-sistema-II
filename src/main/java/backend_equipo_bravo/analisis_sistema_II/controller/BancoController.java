package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.BancoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Banco;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.BancoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/banco")
@Tag(name = "Banco", description = "Endpoints para CRUD banco")
public class BancoController extends BaseController {

    @Autowired
    private BancoService bancoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(bancoService.buscarTodosPermisos(), SuccessCode.BANCO_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(bancoService.buscarPorIdPermisos(id), SuccessCode.BANCO_GENERAL);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody BancoRequest request) {
        try {
            Banco creado = bancoService.crear(request);
            Map<String, Object> data = Map.of("banco", creado);
            return success(data, SuccessCode.BANCO_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody BancoRequest request) {
        try {
            Banco actualizado = bancoService.actualizar(id, request);
            Map<String, Object> data = Map.of("banco", actualizado);
            return success(data, SuccessCode.BANCO_UPDATED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            bancoService.eliminarBasePermisos(id);
            return success(null, SuccessCode.BANCO_DELETED_SUCCESS);
        } catch (BusinessException ex) {
            return error(ex.getCodigoNumerico(), ex.getCodigoTexto(), ex.getMessage(), ex.getCodigoNumerico() == 409 ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND);
        }
    }
}
