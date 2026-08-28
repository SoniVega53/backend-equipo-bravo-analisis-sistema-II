package backend_equipo_bravo.analisis_sistema_II.exception.errorCode;

public enum UsuarioError {

    AUTH_USER_NOT_FOUND(101, "AUTH_USER_NOT_FOUND", "El usuario ingresado no existe.", "El ID de usuario proporcionado no se encuentra en la base de datos."),
    AUTH_USER_EXIST(101, "AUTH_USER_EXIST", "El usuario ingresado ya existe.", "El ID de usuario proporcionado ya se encuentra en la base de datos."),
    AUTH_INVALID_PASSWORD(102, "AUTH_INVALID_PASSWORD", "Credenciales inválidas.", "La contraseña ingresada no coincide con el hash almacenado."),
    AUTH_INVALID_POLICY(109, "AUTH_INVALID_POLICY", "Contraseña inválida. No cumple con las normas de la empresa.", "La contraseña ingresada no cumple las normas de la empresa."),
    AUTH_USER_INACTIVE(103, "AUTH_USER_INACTIVE", "El usuario se encuentra inactivo.", "El usuario tiene un estatus que no le permite ingresar al sistema."),
    AUTH_USER_BLOCKED(108, "AUTH_USER_BLOCKED", "El usuario se encuentra bloqueado.", "El usuario tiene un estatus que no le permite ingresar al sistema."),
    AUTH_USER_BLOCKED_ATTEMPTS(104, "AUTH_USER_BLOCKED_ATTEMPTS", "Usuario bloqueado por exceder los intentos de acceso.", "Superó el límite de intentos permitidos y su estatus cambió a bloqueado."),
    AUTH_TOKEN_EXPIRED(105, "AUTH_TOKEN_EXPIRED", "La sesión ha expirado. Por favor, ingrese de nuevo.", "El JWT enviado ya pasó su tiempo de vida configurado."),
    AUTH_UNAUTHORIZED(106, "AUTH_UNAUTHORIZED", "Acceso denegado: No tiene permisos suficientes.", "El usuario intenta realizar una acción o entrar a un módulo para el que no tiene rol."),
    AUTH_NO_AUTHORIZED(107, "AUTH_NO_AUTHORIZED", "Usuario no logeado correctamente.", "No existe usuario."),
    INVALID_FORMAT(400, "INVALID_FORMAT", "El archivo debe ser una imagen.", ""),
    FILE_ERROR(500, "FILE_ERROR", "Error al guardar el archivo en el servidor.", ""),

    AUTH_NO_AUTHORIZED_VIEW(4001, "AUTH_NO_AUTHORIZED_VIEW", "No tiene Permisos para ver los registros.", "Permisos Insuficientes."),
    AUTH_NO_AUTHORIZED_MODIFY(4002, "AUTH_NO_AUTHORIZED_MODIFY", "No tiene Permisos para modificar el registro.", "Permisos Insuficientes."),
    AUTH_NO_AUTHORIZED_DELETE(4003, "AUTH_NO_AUTHORIZED_DELETE", "No tiene Permisos para eliminar el registro.", "Permisos Insuficientes."),
    AUTH_NO_AUTHORIZED_ADD(4004, "AUTH_NO_AUTHORIZED_ADD", "No tiene Permisos para crear el registro.", "Permisos Insuficientes."),

    AUTH_INVALID_CREDENTIALS_ATTEMPTS(109, "AUTH_INVALID_CREDENTIALS_ATTEMPTS", "Credenciales inválidas. Intento %d de %d.", "El usuario introdujo una contraseña incorrecta y se incrementó el contador de intentos."),
    AUTH_NOT_CHANGE(1400, "AUTH_NOT_CHANGE", "Usuario No Necesita Cambiar Contraseña", ""),
    AUTH_INVALID_ANSWER(400, "AUTH_INVALID_ANSWER", "Respuesta incorrecta, Intento %d de %d.", "La respuesta proporcionada no coincide con nuestros registros."),
    AUTH_NO_QUESTION_CONFIGURED(400, "AUTH_NO_QUESTION", "Pregunta no configurada", "El usuario no tiene una pregunta de seguridad configurada."),
    AUTH_PASSWORD_EMPTY(140, "AUTH_PASSWORD_EMPTY", "Contraseña Vacia", ""),
    AUTH_PASSWORD_NOAUTH(143, "AUTH_PASSWORD_NOAUTH", "Contraseña actual no valida", "Contraseña es incorrecta"),
    AUTH_PASSWORD_EQUALS(1401, "AUTH_PASSWORD_EQUALS", "Contraseña es igual a la anterior", "La nueva contraseña no puede ser igual a la anterior");

    private final int codigoNumerico;
    private final String codigoTexto;
    private final String mensajeFormato;
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