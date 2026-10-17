
package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.BancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/console/bancos")
@Tag(name = "Banco", description = "Consulta de bancos")
public class BancoController extends BaseController {

    @Autowired
    private BancoService bancoService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(
                bancoService.buscarTodosPermisos(),
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
}
