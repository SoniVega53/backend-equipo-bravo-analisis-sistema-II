package backend_equipo_bravo.analisis_sistema_II.exception.errorCode;

public enum FlujoStatusEmpleadoError {

    FLUJO_NOT_FOUND(201, "FLUJO_NOT_FOUND", "El flujo de estatus no existe.", "El flujo de estatus de empleado especificado no se encuentra registrado."),
    FLUJO_ALREADY_EXISTS(202, "FLUJO_ALREADY_EXISTS", "El flujo de estatus ya existe.", "Ya existe una transición configurada para este estatus actual y estatus nuevo."),
    STATUS_ACTUAL_NOT_FOUND(203, "STATUS_ACTUAL_NOT_FOUND", "El estatus actual no existe.", "El estatus actual seleccionado no existe en el catálogo."),
    STATUS_NUEVO_NOT_FOUND(204, "STATUS_NUEVO_NOT_FOUND", "El estatus nuevo no existe.", "El estatus nuevo seleccionado no existe en el catálogo."),
    MISMO_STATUS(205, "MISMO_STATUS", "Transición inválida.", "El estatus actual y el estatus nuevo no pueden ser el mismo."),
    NOMBRE_EVENTO_REQUERIDO(206, "NOMBRE_EVENTO_REQUERIDO", "El nombre del evento es obligatorio.", "Debe proporcionar el nombre del evento que origina el cambio.");

    private final int codigoNumerico;
    private final String codigoTexto;
    private final String mensaje;
    private final String descripcion;

    FlujoStatusEmpleadoError(int codigoNumerico, String codigoTexto, String mensaje, String descripcion) {
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

