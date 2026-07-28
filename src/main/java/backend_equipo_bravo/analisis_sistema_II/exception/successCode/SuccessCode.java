package backend_equipo_bravo.analisis_sistema_II.exception.successCode;

public enum SuccessCode {

    AUTH_LOGIN_SUCCESS(200, "AUTH_LOGIN_SUCCESS", "Autenticación exitosa."),
    USER_CREATED_SUCCESS(201, "USER_CREATED_SUCCESS", "El usuario se creó correctamente."),
    USER_UPDATED_SUCCESS(200, "USER_UPDATED_SUCCESS", "El perfil se actualizó con éxito."),
    SUCURSAL_DELETED_SUCCESS(200, "SUCURSAL_DELETED_SUCCESS", "La sucursal fue eliminada correctamente.");

    private final int codigoNumerico;
    private final String codigoTexto;
    private final String mensaje;

    SuccessCode(int codigoNumerico, String codigoTexto, String mensaje) {
        this.codigoNumerico = codigoNumerico;
        this.codigoTexto = codigoTexto;
        this.mensaje = mensaje;
    }

    public int getCodigoNumerico() { return codigoNumerico; }
    public String getCodigoTexto() { return codigoTexto; }
    public String getMensaje() { return mensaje; }
}