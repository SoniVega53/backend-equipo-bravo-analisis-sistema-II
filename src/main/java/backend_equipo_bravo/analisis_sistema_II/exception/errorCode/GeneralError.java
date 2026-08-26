package backend_equipo_bravo.analisis_sistema_II.exception.errorCode;

public enum GeneralError {

    ERROR_SERVICE(400, "ERROR_SERVICE", "Error servicio.", "Algo salio mal revise su información."),
    ERROR_OPCION_NOT_FOUND(401, "ERROR_OPCION_NOT_FOUND", "La opcion no existe.", "No se encontro ninguna relacion con esta opcion."),
    ERROR_OPCION_PERMISOS_NOT_FOUND(402, "ERROR_OPCION_PERMISOS_NOT_FOUND", "No se encontraron permisos.", "No se encontro ninguna permiso."),

    STATUS_USER_NOT_FOUND(201, "STATUS_USER_NOT_FOUND", "El estatus de usuario no existe.", "El ID de estatus proporcionado no se encuentra registrado."),

    ERROR_DEPENDENCY(409, "ERROR_DEPENDENCY", "El registro no se puede eliminar porque está siendo utilizado en otra parte del sistema.", "");

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