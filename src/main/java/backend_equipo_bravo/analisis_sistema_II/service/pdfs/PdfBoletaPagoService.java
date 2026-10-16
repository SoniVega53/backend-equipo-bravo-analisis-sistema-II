package backend_equipo_bravo.analisis_sistema_II.service.pdfs;

import backend_equipo_bravo.analisis_sistema_II.dto.planilla.BoletaPagoRequestDto;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaDetalleDto;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaResponseDto;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.ReportePlanillaRequestDto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.service.ReportePlanillaService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class PdfBoletaPagoService {

    private static final String[] MESES = {
            "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    @Autowired
    private ReportePlanillaService reportePlanillaService;

    public byte[] generarPdf(BoletaPagoRequestDto request) {
        ReportePlanillaRequestDto reportRequest = new ReportePlanillaRequestDto();
        reportRequest.setAnio(request.getAnio());
        reportRequest.setMes(request.getMes());

        PlanillaResponseDto data = reportePlanillaService.obtenerReporte(reportRequest);

        PlanillaDetalleDto d = data.getDetalles().stream()
                .filter(x -> Objects.equals(x.getIdEmpleado(), request.getIdEmpleado()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));

        BigDecimal base = bd(d.getIngresoSueldoBase());
        BigDecimal bono = bd(d.getIngresoBonificacionDecreto());
        BigDecimal otros = bd(d.getIngresoOtrosIngresos());
        BigDecimal igss = bd(d.getDescuentoIgss());
        BigDecimal isr = bd(d.getDescuentoIsr());
        BigDecimal faltas = bd(d.getDescuentoInasistencias());
        BigDecimal totalIngresos = base.add(bono).add(otros);
        BigDecimal totalDescuentos = igss.add(isr).add(faltas);

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
            Font fontSeccion = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            Font fontCelda = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            Font fontNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
            Font fontNeto = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK);

            DecimalFormat df = new DecimalFormat("Q #,##0.00");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            Paragraph titulo = new Paragraph("BOLETA DE PAGO", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            String nombreMes = (request.getMes() != null && request.getMes() >= 1 && request.getMes() <= 12)
                    ? MESES[request.getMes()] : String.valueOf(request.getMes());
            Paragraph periodo = new Paragraph("Período: " + nombreMes + " " + request.getAnio(), fontSubtitulo);
            periodo.setAlignment(Element.ALIGN_CENTER);
            periodo.setSpacingAfter(20);
            document.add(periodo);

            PdfPTable tablaEmpleado = new PdfPTable(2);
            tablaEmpleado.setWidthPercentage(100);
            tablaEmpleado.setWidths(new float[]{1.2f, 3f});
            tablaEmpleado.setSpacingAfter(20);
            agregarFila(tablaEmpleado, "Empleado:", d.getNombres(), fontNegrita, fontCelda);
            agregarFila(tablaEmpleado, "ID Empleado:", String.valueOf(d.getIdEmpleado()), fontNegrita, fontCelda);
            agregarFila(tablaEmpleado, "Puesto:", d.getPuesto(), fontNegrita, fontCelda);
            agregarFila(tablaEmpleado, "Estado:", d.getStatus(), fontNegrita, fontCelda);
            agregarFila(tablaEmpleado, "Fecha de contratación:",
                    d.getFechaContratacion() != null ? d.getFechaContratacion().format(dtf) : "", fontNegrita, fontCelda);
            document.add(tablaEmpleado);

            document.add(crearSeccion("INGRESOS", new BaseColor(39, 174, 96), fontSeccion,
                    new String[]{"Sueldo base", "Bonificación decreto", "Otros ingresos"},
                    new String[]{df.format(base), df.format(bono), df.format(otros)},
                    "Total ingresos", df.format(totalIngresos), fontCelda, fontNegrita));

            document.add(crearSeccion("DESCUENTOS", new BaseColor(192, 57, 43), fontSeccion,
                    new String[]{"IGSS", "ISR", "Inasistencias"},
                    new String[]{df.format(igss), df.format(isr), df.format(faltas)},
                    "Total descuentos", df.format(totalDescuentos), fontCelda, fontNegrita));

            PdfPTable tablaNeto = new PdfPTable(2);
            tablaNeto.setWidthPercentage(100);
            tablaNeto.setWidths(new float[]{3f, 1.5f});
            tablaNeto.setSpacingBefore(10);
            PdfPCell etiqueta = new PdfPCell(new Phrase("SALARIO NETO A PAGAR", fontNeto));
            etiqueta.setBackgroundColor(new BaseColor(236, 240, 241));
            etiqueta.setPadding(8);
            PdfPCell valor = new PdfPCell(new Phrase(df.format(bd(d.getSalarioNeto())), fontNeto));
            valor.setBackgroundColor(new BaseColor(236, 240, 241));
            valor.setHorizontalAlignment(Element.ALIGN_RIGHT);
            valor.setPadding(8);
            tablaNeto.addCell(etiqueta);
            tablaNeto.addCell(valor);
            document.add(tablaNeto);

            Paragraph firma = new Paragraph("\n\n\n______________________________\nFirma del empleado", fontCelda);
            firma.setAlignment(Element.ALIGN_CENTER);
            document.add(firma);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar la boleta de pago en PDF", e);
        }

        return out.toByteArray();
    }

    private PdfPTable crearSeccion(String titulo, BaseColor color, Font fontSeccion,
                                   String[] conceptos, String[] montos,
                                   String etiquetaTotal, String total,
                                   Font fontCelda, Font fontNegrita) throws DocumentException {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{3f, 1.5f});
        tabla.setSpacingAfter(10);

        PdfPCell cabecera = new PdfPCell(new Phrase(titulo, fontSeccion));
        cabecera.setColspan(2);
        cabecera.setBackgroundColor(color);
        cabecera.setPadding(6);
        tabla.addCell(cabecera);

        for (int i = 0; i < conceptos.length; i++) {
            agregarCelda(tabla, conceptos[i], fontCelda, Element.ALIGN_LEFT);
            agregarCelda(tabla, montos[i], fontCelda, Element.ALIGN_RIGHT);
        }
        agregarCelda(tabla, etiquetaTotal, fontNegrita, Element.ALIGN_LEFT);
        agregarCelda(tabla, total, fontNegrita, Element.ALIGN_RIGHT);
        return tabla;
    }

    private void agregarFila(PdfPTable tabla, String etiqueta, String valor, Font fontEtiqueta, Font fontValor) {
        PdfPCell c1 = new PdfPCell(new Phrase(etiqueta, fontEtiqueta));
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setPadding(3);
        PdfPCell c2 = new PdfPCell(new Phrase(valor != null ? valor : "", fontValor));
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setPadding(3);
        tabla.addCell(c1);
        tabla.addCell(c2);
    }

    private void agregarCelda(PdfPTable tabla, String texto, Font fuente, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setHorizontalAlignment(alineacion);
        celda.setPadding(5);
        tabla.addCell(celda);
    }

    private BigDecimal bd(Object valor) {
        return valor == null ? BigDecimal.ZERO : new BigDecimal(valor.toString());
    }
}