package backend_equipo_bravo.analisis_sistema_II.service.pdfs;

import backend_equipo_bravo.analisis_sistema_II.dto.ReporteGenericoRequestDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class PdfGenericoService {

    public byte[] generarPdf(ReporteGenericoRequestDto request) {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font fontFecha = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
            Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            Font fontCelda = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

            Paragraph titulo = new Paragraph(request.getTitulo().replace("_", " ").toUpperCase(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            Paragraph fecha = new Paragraph("Generado el: " + fechaActual, fontFecha);
            fecha.setAlignment(Element.ALIGN_CENTER);
            fecha.setSpacingAfter(20);
            document.add(fecha);

            if (request.getColumnas() == null || request.getColumnas().isEmpty()) {
                document.add(new Paragraph("No hay datos para mostrar."));
                document.close();
                return out.toByteArray();
            }

            PdfPTable tabla = new PdfPTable(request.getColumnas().size());
            tabla.setWidthPercentage(100);

            // Cabeceras
            for (ReporteGenericoRequestDto.ColumnaDto col : request.getColumnas()) {
                PdfPCell celda = new PdfPCell(new Phrase(col.getHeader(), fontCabecera));
                celda.setBackgroundColor(new BaseColor(41, 128, 185));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setPadding(6);
                tabla.addCell(celda);
            }

            // Filas dinámicas
            boolean fondoAlterno = false;
            for (Map<String, Object> fila : request.getDatos()) {
                BaseColor colorFondo = fondoAlterno ? new BaseColor(245, 245, 245) : BaseColor.WHITE;

                for (ReporteGenericoRequestDto.ColumnaDto col : request.getColumnas()) {
                    Object valorObj = fila.get(col.getField());
                    String valorStr = valorObj != null ? String.valueOf(valorObj) : "";

                    PdfPCell celda = new PdfPCell(new Phrase(valorStr, fontCelda));
                    celda.setBackgroundColor(colorFondo);
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setPadding(5);
                    tabla.addCell(celda);
                }
                fondoAlterno = !fondoAlterno;
            }

            document.add(tabla);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF genérico", e);
        }

        return out.toByteArray();
    }
}