package backend_equipo_bravo.analisis_sistema_II.exception.errorCode;

public enum UsuarioError {

    AUTH_USER_NOT_FOUND(101, "AUTH_USER_NOT_FOUND", "El usuario ingresado no existe.", "El ID de usuario proporcionado no se encuentra en la base de datos."),
    AUTH_INVALID_PASSWORD(102, "AUTH_INVALID_PASSWORD", "Credenciales inválidas.", "La contraseña ingresada no coincide con el hash almacenado."),
    AUTH_USER_INACTIVE(103, "AUTH_USER_INACTIVE", "El usuario se encuentra inactivo.", "El usuario tiene un estatus que no le permite ingresar al sistema."),
    AUTH_USER_BLOCKED(103, "AUTH_USER_BLOCKED", "El usuario se encuentra bloqueado.", "El usuario tiene un estatus que no le permite ingresar al sistema."),
    AUTH_USER_BLOCKED_ATTEMPTS(104, "AUTH_USER_BLOCKED_ATTEMPTS", "Usuario bloqueado por exceder los intentos de acceso.", "Superó el límite de intentos permitidos y su estatus cambió a bloqueado."),
    AUTH_TOKEN_EXPIRED(105, "AUTH_TOKEN_EXPIRED", "La sesión ha expirado. Por favor, ingrese de nuevo.", "El JWT enviado ya pasó su tiempo de vida configurado."),
    AUTH_UNAUTHORIZED(106, "AUTH_UNAUTHORIZED", "Acceso denegado: No tiene permisos suficientes.", "El usuario intenta realizar una acción o entrar a un módulo para el que no tiene rol."),
    AUTH_NO_AUTHORIZED(107, "AUTH_NO_AUTHORIZED", "Usuario no logeado correctamente.", "No existe usuario."),
    // Nuevo error con formato dinámico para los intentos
    AUTH_INVALID_CREDENTIALS_ATTEMPTS(107, "AUTH_INVALID_CREDENTIALS_ATTEMPTS", "Credenciales inválidas. Intento %d de %d.", "El usuario introdujo una contraseña incorrecta y se incrementó el contador de intentos.");

    private final int codigoNumerico;
    private final String codigoTexto;
    private final String mensajeFormato; // Mensaje con los comodines %d
    private final String descripcion;

    UsuarioError(int codigoNumerico, String codigoTexto, String mensajeFormato, String descripcion) {
        this.codigoNumerico = codigoNumerico;
        this.codigoTexto = codigoTexto;
        this.mensajeFormato = mensajeFormato;
        this.descripcion = descripcion;
    }

    public int getCodigoNumerico() {
        return codigoNumerico;
    }

    public String getCodigoTexto() {
        return codigoTexto;
    }

    public String getMensajeFormato() {
        return mensajeFormato;
    }

    public String getDescripcion() {
        return descripcion;
    }

}