package backend_equipo_bravo.analisis_sistema_II.exception.errorCode;

public enum SucursalError {
    SUCURSAL_NOT_FOUND(201, "SUCURSAL_NOT_FOUND", "La sucursal no encontrada.", "El ID de sucursal proporcionado no se encuentra registrado.");

    private final int codigoNumerico;
    private final String codigoTexto;
    private final String mensaje;
    private final String descripcion;

    SucursalError(int codigoNumerico, String codigoTexto, String mensaje, String descripcion) {
        this.codigoNumerico = codigoNumerico;
        this.codigoTexto = codigoTexto;
        this.mensaje = mensaje;
        this.descripcion = descripcion;
    }

    public int getCodigoNumerico() {
        return codigoNumerico;
    }

    public String getCodigoTexto() {
        return codigoTexto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
