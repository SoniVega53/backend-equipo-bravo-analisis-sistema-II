package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalogos")
@CrossOrigin(origins = "*")
public class CatalogoController extends BaseController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/empresas")
    public ResponseEntity<?> getEmpresas() {
        return success(catalogoService.getEmpresas(), SuccessCode.GENERAL);
    }

    @GetMapping("/sucursales")
    public ResponseEntity<?> getSucursales() {
        return success(catalogoService.getSucursales(), SuccessCode.GENERAL);
    }

    @GetMapping("/sucursales/{idEmpresa}")
    public ResponseEntity<?> getSucursales(@PathVariable Integer idEmpresa) {
        return success(catalogoService.getSucursalesEmpresa(idEmpresa), SuccessCode.GENERAL);
    }

    @GetMapping("/generos")
    public ResponseEntity<?> getGeneros() {
        return success(catalogoService.getGeneros(), SuccessCode.GENERAL);
    }

    @GetMapping("/status-usuario")
    public ResponseEntity<?> getStatusUsuario() {
        return success(catalogoService.getStatusUsuario(), SuccessCode.GENERAL);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return success(catalogoService.getRoles(), SuccessCode.GENERAL);
    }
}