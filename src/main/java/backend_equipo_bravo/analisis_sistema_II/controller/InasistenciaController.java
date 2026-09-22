package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.inasistencia.InasistenciaRequestDto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.InasistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/console/inasistencias")
public class InasistenciaController extends BaseController {

    @Autowired
    private InasistenciaService inasistenciaService;

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            return success(inasistenciaService.listarTodasPermisos(), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<?> listarPorEmpleado(@PathVariable Integer idEmpleado) {
        try {
            return success(inasistenciaService.listarTodasEmpleadoPermisos(idEmpleado), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody InasistenciaRequestDto request) {
        try {
            return success(inasistenciaService.crearInasistencia(request), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody InasistenciaRequestDto request) {
        try {
            return success(inasistenciaService.actualizarInasistencia(id, request), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            return success(inasistenciaService.eliminarInasistencia(id), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}