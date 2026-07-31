package backend_equipo_bravo.analisis_sistema_II.controller;


import backend_equipo_bravo.analisis_sistema_II.dto.MenuItemDto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController extends BaseController {

    @GetMapping()
    public ResponseEntity<?> obtenerMenuUserRol() {
        try {
            List<MenuItemDto> menuItems = List.of(
                    MenuItemDto.builder()
                            .id(1L)
                            .label("Seguridad")
                            .expanded(true)
                            .children(List.of(
                                    MenuItemDto.builder()
                                            .id(1L)
                                            .label("Parametros Generales")
                                            .expanded(true)
                                            .children(List.of(
                                                    MenuItemDto.builder()
                                                            .id(1L)
                                                            .label("Empresas")
                                                            .url("empresa")
                                                            .build(),
                                                    MenuItemDto.builder()
                                                            .id(2L)
                                                            .label("Sucursales")
                                                            .url("sucursal")
                                                            .build(),
                                                    MenuItemDto.builder()
                                                            .id(3L)
                                                            .label("Generos")
                                                            .url("genero")
                                                            .build()
                                            ))
                                            .build()
                            ))
                            .build()
            );

            return success(menuItems, SuccessCode.MENU_AUTH_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
