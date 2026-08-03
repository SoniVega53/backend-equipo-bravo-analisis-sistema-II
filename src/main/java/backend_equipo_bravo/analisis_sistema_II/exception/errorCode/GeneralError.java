package backend_equipo_bravo.analisis_sistema_II.exception.errorCode;

public enum GeneralError {

    ERROR_SERVICE(400, "ERROR_SERVICE", "Error servicio.", "Algo salio mal revise su infomracion.");

    private final int codigoNumerico;
    private final String codigoTexto;
    private final String mensaje;
    private final String descripcion;

    GeneralError(int codigoNumerico, String codigoTexto, String mensaje, String descripcion) {
        this.codigoNumerico = codigoNumerico;
        this.codigoTexto = codigoTexto;
        this.mensaje = mensaje;
        this.descripcion = descripcion;
    }

    public int getCodigoNumerico() { return codigoNumerico; }
    public String getCodigoTexto() { return codigoTexto; }
    public String getMensaje() { return mensaje; }
    public String getDescripcion() { return descripcion; }
}