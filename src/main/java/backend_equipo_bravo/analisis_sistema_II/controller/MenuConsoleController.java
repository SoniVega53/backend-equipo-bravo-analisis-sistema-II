package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.MenuRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Menu;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.MenuConsoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/menu")
@Tag(name = "Menu", description = "Endpoints para CRUD de Menú")
public class MenuConsoleController extends BaseController {

    @Autowired
    private MenuConsoleService menuService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(menuService.buscarTodosPermisos(), SuccessCode.MENU_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(menuService.buscarPorIdPermisos(id), SuccessCode.MENU_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody MenuRequest menu) {
        try {
            Menu creado = menuService.crear(menu);
            Map<String, Object> data = Map.of("menu", creado);
            return success(data, SuccessCode.MENU_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody MenuRequest menu) {
        try {
            Menu actualizado = menuService.actualizar(id, menu);
            Map<String, Object> data = Map.of("menu", actualizado);
            return success(data, SuccessCode.MENU_UPDATED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            menuService.eliminarBasePermisos(id);
            return success(null, SuccessCode.MENU_DELETED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}