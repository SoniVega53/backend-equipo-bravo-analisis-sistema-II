package backend_equipo_bravo.analisis_sistema_II.utils;

public enum FormatoFecha {
    YYYY_MM_DD("yyyy-MM-dd"),
    DD_MM_YYYY("dd/MM/yyyy"),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    DD_MM_YYYY_HH_MM("dd/MM/yyyy HH:mm"),
    ISO_8601("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private final String patron;

    FormatoFecha(String patron) {
        this.patron = patron;
    }

    public String getPatron() {
        return patron;
    }
}