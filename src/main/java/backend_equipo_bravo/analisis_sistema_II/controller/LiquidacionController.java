package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.liquidacion.LiquidacionDto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.LiquidacionService;
import backend_equipo_bravo.analisis_sistema_II.service.pdfs.PdfLiquidacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/console/liquidacion")
public class LiquidacionController extends BaseController {

    @Autowired
    private LiquidacionService liquidacionService;

    @Autowired
    private PdfLiquidacionService pdfLiquidacionService;

    @PostMapping("/procesar")
    public ResponseEntity<?> procesarLiquidacion(@RequestBody LiquidacionDto request) {
        try {
            return success(liquidacionService.procesarLiquidacion(request), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodas() {
        try {
            return success(liquidacionService.obtenerTodas(), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return success(liquidacionService.obtenerPorIdLiquidacion(id), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<?> obtenerPorEmpleado(@PathVariable Integer idEmpleado) {
        try {
            return success(liquidacionService.obtenerPorEmpleado(idEmpleado), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/empleado/{idEmpleado}/base")
    public ResponseEntity<?> obtenerEmpleadoBase(@PathVariable Integer idEmpleado) {
        try {
            return success(liquidacionService.obtenerEmpleadoBase(idEmpleado), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/empleados")
    public ResponseEntity<?> listarEmpleadosBase() {
        try {
            return success(liquidacionService.listarEmpleadosBase(), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/empleado/{idEmpleado}/historial")
    public ResponseEntity<?> obtenerHistorialPorEmpleado(@PathVariable Integer idEmpleado) {
        try {
            return success(liquidacionService.obtenerHistorialPorEmpleado(idEmpleado), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}/pdf", produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarBoletaPdf(@PathVariable Integer id) {
        try {
            byte[] pdfContent = pdfLiquidacionService.generarBoletaPdf(id);
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "boleta_liquidacion_" + id + ".pdf");
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}