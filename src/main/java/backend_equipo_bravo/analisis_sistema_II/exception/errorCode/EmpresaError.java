package backend_equipo_bravo.analisis_sistema_II.exception.errorCode;

public enum EmpresaError {

    EMPRESA_NOT_FOUND(201, "EMPRESA_NOT_FOUND", "La empresa no existe.", "El ID de empresa proporcionado no se encuentra registrado."),
    EMPRESA_DUPLICATE_NIT(202, "EMPRESA_DUPLICATE_NIT", "El NIT ya está registrado.", "Ya existe otra empresa dada de alta con el mismo número de NIT."),
    EMPRESA_INACTIVE(203, "EMPRESA_INACTIVE", "La empresa está inactiva.", "No se pueden realizar operaciones en empresas con estatus inactivo.");

    private final int codigoNumerico;
    private final String codigoTexto;
    private final String mensaje;
    private final String descripcion;

    EmpresaError(int codigoNumerico, String codigoTexto, String mensaje, String descripcion) {
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