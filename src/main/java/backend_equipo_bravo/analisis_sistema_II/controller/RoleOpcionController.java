package backend_equipo_bravo.analisis_sistema_II.controller;


import backend_equipo_bravo.analisis_sistema_II.dto.RoleOptionDto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/OpRole")
public class RoleOpcionController  extends BaseController{

    @GetMapping("/{page}")
    public ResponseEntity<?> obtenerPermisosRol(@PathVariable String page) {
        try {
            RoleOptionDto role = RoleOptionDto.builder()
                    .consultar(true)
                    .alta(true)
                    .baja(true)
                    .cambio(true)
                    .imprimir(true)
                    .exportar(true)
                    .build();

            Map<String, RoleOptionDto> data = new HashMap<>();
            data.put("permisos",role);

            return success(data, SuccessCode.USER_AUTH_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
