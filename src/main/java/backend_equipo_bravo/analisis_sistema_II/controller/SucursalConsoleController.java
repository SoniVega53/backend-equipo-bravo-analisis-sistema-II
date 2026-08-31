package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.SucursalRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Sucursal;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.SucursalConsoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/console/sucursal")
@Tag(name = "Sucursal", description = "Endpoints para CRUD sucursal")
public class SucursalConsoleController extends BaseController {

    @Autowired
    private SucursalConsoleService sucursalService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return success(sucursalService.buscarTodosPermisos(), SuccessCode.SUCURSAL_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(sucursalService.buscarPorIdPermisos(id), SuccessCode.SUCURSAL_GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody SucursalRequest sucursal) {
        try {
            Sucursal creada = sucursalService.crear(sucursal);
            Map<String, Object> data = Map.of("sucursal", creada);
            return success(data, SuccessCode.SUCURSAL_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody SucursalRequest sucursal) {
        try {
            Sucursal actualizada = sucursalService.actualizar(id, sucursal);
            Map<String, Object> data = Map.of("sucursal", actualizada);
            return success(data, SuccessCode.SUCURSAL_UPDATED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            sucursalService.eliminarBasePermisos(id);
            return success(null, SuccessCode.SUCURSAL_DELETED_SUCCESS);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}