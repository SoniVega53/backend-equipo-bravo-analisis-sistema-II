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

    @GetMapping("/modulos")
    public ResponseEntity<?> getModulos() {
        return success(catalogoService.getModulos(), SuccessCode.GENERAL);
    }

    @GetMapping("/menus")
    public ResponseEntity<?> getMenus() {
        return success(catalogoService.getMenus(), SuccessCode.GENERAL);
    }

    @GetMapping("/menus/{idModulo}")
    public ResponseEntity<?> getMenus(@PathVariable Integer idModulo) {
        return success(catalogoService.getMenus(idModulo), SuccessCode.GENERAL);
    }

    @GetMapping("/estados-civiles")
    public ResponseEntity<?> getEstadosCiviles() {
        return success(catalogoService.getEstadosCiviles(), SuccessCode.GENERAL);
    }

    @GetMapping("/status-empleados")
    public ResponseEntity<?> getStatusEmpleados() {
        return success(catalogoService.getStatusEmpleados(), SuccessCode.GENERAL);
    }

    @GetMapping("/tipos-documentos")
    public ResponseEntity<?> getTiposDocumentos() {
        return success(catalogoService.getTiposDocumentos(), SuccessCode.GENERAL);
    }

    @GetMapping("/departamentos")
    public ResponseEntity<?> getDepartamentos() {
        return success(catalogoService.getDepartamentos(), SuccessCode.GENERAL);
    }

    @GetMapping("/puestos")
    public ResponseEntity<?> getPuestos() {
        return success(catalogoService.getPuestos(), SuccessCode.GENERAL);
    }

    @GetMapping("/puestos/departamento/{idDepartamento}")
    public ResponseEntity<?> getPuestosDepartamento(@PathVariable Integer idDepartamento) {
        return success(catalogoService.getPuestosDepartamento(idDepartamento), SuccessCode.GENERAL);
    }

    @GetMapping("/personas")
    public ResponseEntity<?> getPersonas() {
        return success(catalogoService.getPersonas(), SuccessCode.GENERAL);
    }

    @GetMapping("/bancos")
    public ResponseEntity<?> getBancos() {
        return success(catalogoService.getBancos(), SuccessCode.GENERAL);
    }

    @GetMapping("/empleados")
    public ResponseEntity<?> getEmpleados() {
        return success(catalogoService.getEmpleados(), SuccessCode.GENERAL);
    }

    @GetMapping("/periodos-planilla")
    public ResponseEntity<?> getPeriodosPlanilla() {
        return success(catalogoService.getPeriodosPlanilla(), SuccessCode.GENERAL);
    }
}