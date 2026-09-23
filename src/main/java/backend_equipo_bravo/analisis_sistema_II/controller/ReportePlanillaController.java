package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.planilla.ReportePlanillaRequestDto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import backend_equipo_bravo.analisis_sistema_II.service.ReportePlanillaService;
import backend_equipo_bravo.analisis_sistema_II.service.pdfs.PdfPlanillaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/console/reporte-planilla")
public class ReportePlanillaController extends BaseController {

    @Autowired
    private ReportePlanillaService reportePlanillaService;

    @Autowired
    private PdfPlanillaService pdfPlanillaService;

    @PostMapping("/generar")
    public ResponseEntity<?> generarReporte(@RequestBody ReportePlanillaRequestDto request) {
        try {
            return success(reportePlanillaService.obtenerReporte(request), SuccessCode.GENERAL);
        } catch (BusinessException e) {
            return error(e.getCodigoNumerico(), e.getCodigoTexto(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/generar-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generarPdf(@RequestBody ReportePlanillaRequestDto request) {
        try {
            byte[] pdfContent = pdfPlanillaService.generarPdf(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "reporte_planilla_" + request.getAnio() + "_" + request.getMes() + ".pdf";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}