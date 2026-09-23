package backend_equipo_bravo.analisis_sistema_II.exception.errorCode;

public enum GeneralError {

    ERROR_SERVICE(400, "ERROR_SERVICE", "Error servicio.", "Algo salio mal revise su información."),
    ERROR_OPCION_NOT_FOUND(401, "ERROR_OPCION_NOT_FOUND", "La opcion no existe.", "No se encontro ninguna relacion con esta opcion."),
    ERROR_OPCION_PERMISOS_NOT_FOUND(402, "ERROR_OPCION_PERMISOS_NOT_FOUND", "No se encontraron permisos.", "No se encontro ninguna permiso."),

    ERROR_EMPLEADO_NOT_FOUND(402, "ERROR_EMPLEADO_NOT_FOUND", "Empleado no existe.", ""),
    ERROR_PERSONA_NOT_FOUND(402, "ERROR_PERSONA_NOT_FOUND", "Persona no existe.", ""),
    ERROR_PUESTO_NOT_FOUND(402, "ERROR_PUESTO_NOT_FOUND", "Puesto no existe.", ""),
    ERROR_STATUS_NOT_FOUND(402, "ERROR_STATUS_NOT_FOUND", "estado no existe.", ""),

    STATUS_USER_NOT_FOUND(201, "STATUS_USER_NOT_FOUND", "El estatus de usuario no existe.", "El ID de estatus proporcionado no se encuentra registrado."),

    ROLE_NOT_FOUND(201, "ROLE_NOT_FOUND", "El rol solicitado no fue encontrado.", ""),

    MODULO_NOT_FOUND(404, "MODULO_NOT_FOUND", "El módulo solicitado no fue encontrado.",""),
    MENU_NOT_FOUND(404, "MENU_NOT_FOUND", "El menú solicitado no fue encontrado.", ""),
    OPCION_NOT_FOUND(404, "OPCION_NOT_FOUND", "La opción solicitada no fue encontrada.", ""),

    PLANILLA_NOT_FOUND(1400, "PLANILLA_NOT_FOUND", "No se encontro ningun registro.", ""),

    INASISTENCIA_PROCESS(1401, "INASISTENCIA_PROCESS", "No se puede eliminar una inasistencia que ya ha sido procesada en la planilla.", ""),


    RANGO_FECHAS_INVALIDO(400, "RANGO_FECHAS_INVALIDO", "Rango de fechas inválido.", "La fecha final no puede ser anterior a la fecha inicial."),
    SOLAPAMIENTO_INASISTENCIA(400, "SOLAPAMIENTO_INASISTENCIA", "Conflicto de fechas.", "El empleado ya posee una inasistencia registrada que se cruza con el rango seleccionado."),

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