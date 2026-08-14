package backend_equipo_bravo.analisis_sistema_II.controller;


import backend_equipo_bravo.analisis_sistema_II.dto.MenuItemDto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu", description = "Endpoints para la gestión del menú del sistema")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;

    @GetMapping()
    public ResponseEntity<?> obtenerMenuUserRol() {
        try {
            return success(menuService.getMenuCompleto(), SuccessCode.MENU_AUTH_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
