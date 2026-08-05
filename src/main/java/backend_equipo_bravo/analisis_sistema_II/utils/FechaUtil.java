package backend_equipo_bravo.analisis_sistema_II.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FechaUtil {

    public static String formatear(LocalDateTime fecha) {
        return formatear(fecha, FormatoFecha.YYYY_MM_DD);
    }

    public static String formatear(LocalDateTime fecha, FormatoFecha formato) {
        if (fecha == null) return null;

        FormatoFecha formatoFinal = (formato != null) ? formato : FormatoFecha.YYYY_MM_DD;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatoFinal.getPatron());

        return fecha.format(formatter);
    }

    public static boolean esValida(String fechaStr, FormatoFecha formato) {
        if (fechaStr == null || fechaStr.trim().isEmpty() || formato == null) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato.getPatron());
            formatter.parse(fechaStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}