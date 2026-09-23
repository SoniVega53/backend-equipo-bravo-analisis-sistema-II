package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaRequestDto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.CalculoPlanillaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/console/calculo-planilla")
public class CalculoPlanillaController extends BaseController {

    @Autowired
    private CalculoPlanillaService calculoPlanillaService;

    @PostMapping("/procesar")
    public ResponseEntity<?> procesarOObtener(@RequestBody PlanillaRequestDto request) {
        try {
            return success(calculoPlanillaService.procesarOObtenerPlanilla(request), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}