package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.periodo_planilla.PeriodoPlanillaDto;
import backend_equipo_bravo.analisis_sistema_II.service.PeriodoPlanillaService;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/console/periodo-planilla")
public class PeriodoPlanillaController extends BaseController {

    @Autowired
    private PeriodoPlanillaService periodoPlanillaService;

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            return success(periodoPlanillaService.obtenerTodos(), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{anio}/{mes}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer anio, @PathVariable Integer mes) {
        try {
            return success(periodoPlanillaService.obtenerPorId(anio, mes), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PeriodoPlanillaDto dto) {
        try {
            return success(periodoPlanillaService.guardar(dto), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{anio}/{mes}")
    public ResponseEntity<?> actualizar(@PathVariable Integer anio, @PathVariable Integer mes, @RequestBody PeriodoPlanillaDto dto) {
        try {
            return success(periodoPlanillaService.actualizar(anio, mes, dto), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{anio}/{mes}")
    public ResponseEntity<?> eliminar(@PathVariable Integer anio, @PathVariable Integer mes) {
        try {
            periodoPlanillaService.eliminar(anio, mes);
            return success("Periodo eliminado correctamente", SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
