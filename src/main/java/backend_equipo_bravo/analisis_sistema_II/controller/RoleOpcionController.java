package backend_equipo_bravo.analisis_sistema_II.controller;


import backend_equipo_bravo.analisis_sistema_II.dto.RoleOption.RoleOpcionListadoDto;
import backend_equipo_bravo.analisis_sistema_II.dto.RoleOption.RoleOpcionRequest;
import backend_equipo_bravo.analisis_sistema_II.dto.RoleOption.RoleOptionDto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.RoleOpcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/OpRole")
public class RoleOpcionController  extends BaseController{

    @Autowired
    private RoleOpcionService roleOpcionService;

    @GetMapping("/{code}")
    public ResponseEntity<?> obtenerPermisosRol(@PathVariable Integer code) {
        try {
            Map<String, RoleOptionDto> data = new HashMap<>();
            data.put("permisos",roleOpcionService.getAuthPageRole(code));
            return success(data, SuccessCode.USER_AUTH_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tabla/{idRole}/{idModule}")
    public ResponseEntity<?> obtenerPermisosPorRol(@PathVariable Integer idRole,@PathVariable Integer idModule) {
        try {
            List<RoleOpcionListadoDto> lista = roleOpcionService.obtenerMatrizPermisos(idRole,idModule);
            return success(lista, SuccessCode.ROLE_OPCION_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/modulo_role")
    public ResponseEntity<?> obtenerModulos() {
        try {
            Map<String,Object> response = new HashMap<>();
            response.put("modulos",roleOpcionService.obtenerModulos());
            response.put("roles",roleOpcionService.obtenerRoles());

            return success(response, SuccessCode.ROLE_OPCION_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modificarTabla")
    public ResponseEntity<?> actualizarPermisos(@RequestBody RoleOpcionRequest request) {
        try {
            roleOpcionService.updateAndSave(request);
            return success("Operacion realizada con exito", SuccessCode.ROLE_OPCION_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
