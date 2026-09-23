package backend_equipo_bravo.analisis_sistema_II.service.pdfs;

import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaDetalleDto;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.PlanillaResponseDto;
import backend_equipo_bravo.analisis_sistema_II.dto.planilla.ReportePlanillaRequestDto;
import backend_equipo_bravo.analisis_sistema_II.service.ReportePlanillaService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfPlanillaService {

    @Autowired
    private ReportePlanillaService reportePlanillaService;

    public byte[] generarPdf(ReportePlanillaRequestDto request) {
        PlanillaResponseDto data = reportePlanillaService.obtenerReporte(request);
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
            Font fontCabeceraTabla = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            Font fontCelda = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            Font fontResumen = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);

            DecimalFormat df = new DecimalFormat("Q #,##0.00");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            Paragraph titulo = new Paragraph("REPORTE DE PLANILLA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph periodo = new Paragraph("Período: " + data.getMes() + " / " + data.getAnio(), fontSubtitulo);
            periodo.setAlignment(Element.ALIGN_CENTER);
            document.add(periodo);

            Paragraph periodo2 = new Paragraph("Procesado: " +  formatearFecha(data.getFechaHoraProcesada()), fontSubtitulo);
            periodo2.setAlignment(Element.ALIGN_CENTER);
            periodo2.setSpacingAfter(20);
            document.add(periodo2);

            PdfPTable tablaResumen = new PdfPTable(3);
            tablaResumen.setWidthPercentage(100);
            tablaResumen.setSpacingAfter(20);

            PdfPCell celdaIngresos = new PdfPCell(new Phrase("Total Ingresos: " + df.format(data.getTotalIngresos()), fontResumen));
            celdaIngresos.setBorder(Rectangle.NO_BORDER);
            celdaIngresos.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell celdaDescuentos = new PdfPCell(new Phrase("Total Descuentos: " + df.format(data.getTotalDescuentos()), fontResumen));
            celdaDescuentos.setBorder(Rectangle.NO_BORDER);
            celdaDescuentos.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell celdaNeto = new PdfPCell(new Phrase("Salario Neto Total: " + df.format(data.getSalarioNeto()), fontResumen));
            celdaNeto.setBorder(Rectangle.NO_BORDER);
            celdaNeto.setHorizontalAlignment(Element.ALIGN_RIGHT);

            tablaResumen.addCell(celdaIngresos);
            tablaResumen.addCell(celdaDescuentos);
            tablaResumen.addCell(celdaNeto);
            document.add(tablaResumen);

            PdfPTable tablaDetalles = new PdfPTable(12);
            tablaDetalles.setWidthPercentage(100);
            tablaDetalles.setWidths(new float[]{1f, 3f, 1.5f,1f,1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});

            String[] cabeceras = {"ID", "Empleado","Puesto","Estado", "Contratación", "Sueldo Base", "Bono", "Otros Ing.", "IGSS", "ISR", "Faltas", "Neto"};
            for (String cabecera : cabeceras) {
                PdfPCell celda = new PdfPCell(new Phrase(cabecera, fontCabeceraTabla));
                celda.setBackgroundColor(new BaseColor(41, 128, 185));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setPadding(5);
                tablaDetalles.addCell(celda);
            }

            boolean fondoAlterno = false;
            for (PlanillaDetalleDto detalle : data.getDetalles()) {
                BaseColor colorFondo = fondoAlterno ? new BaseColor(245, 245, 245) : BaseColor.WHITE;

                agregarCelda(tablaDetalles, String.valueOf(detalle.getIdEmpleado()), fontCelda, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tablaDetalles, detalle.getNombres(), fontCelda, colorFondo, Element.ALIGN_LEFT);

                agregarCelda(tablaDetalles, detalle.getPuesto(), fontCelda, colorFondo, Element.ALIGN_LEFT);
                agregarCelda(tablaDetalles, detalle.getStatus(), fontCelda, colorFondo, Element.ALIGN_LEFT);

                agregarCelda(tablaDetalles, detalle.getFechaContratacion().format(dtf), fontCelda, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tablaDetalles, df.format(detalle.getIngresoSueldoBase()), fontCelda, colorFondo, Element.ALIGN_RIGHT);
                agregarCelda(tablaDetalles, df.format(detalle.getIngresoBonificacionDecreto()), fontCelda, colorFondo, Element.ALIGN_RIGHT);
                agregarCelda(tablaDetalles, df.format(detalle.getIngresoOtrosIngresos()), fontCelda, colorFondo, Element.ALIGN_RIGHT);
                agregarCelda(tablaDetalles, df.format(detalle.getDescuentoIgss()), fontCelda, colorFondo, Element.ALIGN_RIGHT);
                agregarCelda(tablaDetalles, df.format(detalle.getDescuentoIsr()), fontCelda, colorFondo, Element.ALIGN_RIGHT);
                agregarCelda(tablaDetalles, df.format(detalle.getDescuentoInasistencias()), fontCelda, colorFondo, Element.ALIGN_RIGHT);

                PdfPCell celdaNetoDetalle = new PdfPCell(new Phrase(df.format(detalle.getSalarioNeto()), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.BLACK)));
                celdaNetoDetalle.setBackgroundColor(colorFondo);
                celdaNetoDetalle.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celdaNetoDetalle.setPadding(5);
                tablaDetalles.addCell(celdaNetoDetalle);

                fondoAlterno = !fondoAlterno;
            }

            document.add(tablaDetalles);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el documento PDF", e);
        }

        return out.toByteArray();
    }

    private void agregarCelda(PdfPTable tabla, String texto, Font fuente, BaseColor colorFondo, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(colorFondo);
        celda.setHorizontalAlignment(alineacion);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        tabla.addCell(celda);
    }

    public String formatearFecha(LocalDateTime fecha) {
        if (fecha == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fecha.format(formatter);
    }
}