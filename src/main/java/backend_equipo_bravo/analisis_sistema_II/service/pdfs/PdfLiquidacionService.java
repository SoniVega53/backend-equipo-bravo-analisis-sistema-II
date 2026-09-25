package backend_equipo_bravo.analisis_sistema_II.service.pdfs;

import backend_equipo_bravo.analisis_sistema_II.dto.liquidacion.LiquidacionDto;

import backend_equipo_bravo.analisis_sistema_II.service.LiquidacionService;
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

@Service
public class PdfLiquidacionService {

    @Autowired
    private LiquidacionService liquidacionService;

    public byte[] generarBoletaPdf(Integer idLiquidacion) {
        LiquidacionDto data = liquidacionService.obtenerPorIdLiquidacion(idLiquidacion);
        Document document = new Document(PageSize.LETTER, 50, 50, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
            Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);

            DecimalFormat df = new DecimalFormat("Q #,##0.00");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            Paragraph titulo = new Paragraph("BOLETA DE LIQUIDACIÓN", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setSpacingAfter(20);

            agregarCeldaSinBorde(tablaInfo, "Empleado: " + safeString(data.getNombreEmpleado()), fontNormal);
            agregarCeldaSinBorde(tablaInfo, "Puesto: " + safeString(data.getNombrePuesto()), fontNormal);
            if (data.getNombreDepartamento() != null && !data.getNombreDepartamento().isBlank()) {
                agregarCeldaSinBorde(tablaInfo, "Departamento: " + safeString(data.getNombreDepartamento()), fontNormal);
            }
            agregarCeldaSinBorde(tablaInfo, "Fecha Contratación: " + (data.getFechaContratacion() != null ? data.getFechaContratacion().format(dtf) : "N/A"), fontNormal);
            agregarCeldaSinBorde(tablaInfo, "Fecha Egreso: " + (data.getFechaEgreso() != null ? data.getFechaEgreso().format(dtf) : "N/A"), fontNormal);
            agregarCeldaSinBorde(tablaInfo, "Motivo Egreso: " + safeString(data.getMotivoEgreso()), fontNormal);
            agregarCeldaSinBorde(tablaInfo, "Fecha Proceso: " + (data.getFechaLiquidacion() != null ? data.getFechaLiquidacion().format(dtf) : "N/A"), fontNormal);
            document.add(tablaInfo);

            PdfPTable tablaDesglose = new PdfPTable(2);
            tablaDesglose.setWidthPercentage(100);
            tablaDesglose.setSpacingAfter(20);

            PdfPCell celdaIngresos = new PdfPCell(new Phrase("INGRESOS", fontSubtitulo));
            celdaIngresos.setBackgroundColor(new BaseColor(240, 240, 240));
            celdaIngresos.setPadding(8);
            tablaDesglose.addCell(celdaIngresos);

            PdfPCell celdaDescuentos = new PdfPCell(new Phrase("DESCUENTOS", fontSubtitulo));
            celdaDescuentos.setBackgroundColor(new BaseColor(240, 240, 240));
            celdaDescuentos.setPadding(8);
            tablaDesglose.addCell(celdaDescuentos);

            tablaDesglose.addCell(crearCeldaDetalle("Sueldo Base", formatMonto(df, data.getIngresoSueldoBase()), fontNormal));
            tablaDesglose.addCell(crearCeldaDetalle("Descuento IGSS", formatMonto(df, data.getDescuentoIgss()), fontNormal));

            tablaDesglose.addCell(crearCeldaDetalle("Bonificación Decreto", formatMonto(df, data.getIngresoBonificacionDecreto()), fontNormal));
            tablaDesglose.addCell(crearCeldaDetalle("Descuento ISR", formatMonto(df, data.getDescuentoIsr()), fontNormal));

            tablaDesglose.addCell(crearCeldaDetalle("Otros Ingresos", formatMonto(df, data.getIngresoOtrosIngresos()), fontNormal));
            tablaDesglose.addCell(crearCeldaDetalle("Inasistencias", formatMonto(df, data.getDescuentoInasistencias()), fontNormal));

            document.add(tablaDesglose);

            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(100);

            tablaTotales.addCell(crearCeldaDetalle("Total Ingresos:", formatMonto(df, data.getTotalIngresos()), fontBold));
            tablaTotales.addCell(crearCeldaDetalle("Total Descuentos:", formatMonto(df, data.getTotalDescuentos()), fontBold));
            document.add(tablaTotales);

            Paragraph neto = new Paragraph("TOTAL LIQUIDADO: " + formatMonto(df, data.getTotalNeto()), fontTitulo);
            neto.setAlignment(Element.ALIGN_RIGHT);
            neto.setPaddingTop(20);
            document.add(neto);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
        return out.toByteArray();
    }

    private void agregarCeldaSinBorde(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setBorder(Rectangle.NO_BORDER);
        celda.setPadding(5);
        tabla.addCell(celda);
    }

    private PdfPCell crearCeldaDetalle(String concepto, String monto, Font font) {
        PdfPTable innerTable = new PdfPTable(2);
        try {
            innerTable.setWidths(new float[]{7f, 3f});
            PdfPCell c1 = new PdfPCell(new Phrase(concepto, font));
            c1.setBorder(Rectangle.NO_BORDER);
            PdfPCell c2 = new PdfPCell(new Phrase(monto, font));
            c2.setBorder(Rectangle.NO_BORDER);
            c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innerTable.addCell(c1);
            innerTable.addCell(c2);
        } catch (DocumentException ignored) {}

        PdfPCell celda = new PdfPCell(innerTable);
        celda.setPadding(8);
        return celda;
    }

    private String safeString(String text) {
        return (text != null && !text.isBlank()) ? text : "N/A";
    }

    private String formatMonto(DecimalFormat df, BigDecimal monto) {
        return df.format(monto != null ? monto : BigDecimal.ZERO);
    }
}