package backend_equipo_bravo.analisis_sistema_II.exception.successCode;

public enum SuccessCode {

    GENERAL(200, "GENERAL", "Acción exitosa."),
    AUTH_LOGIN_SUCCESS(200, "AUTH_LOGIN_SUCCESS", "Autenticación exitosa."),
    AUTH_PERFIL_SUCCESS(200, "AUTH_PERFIL_SUCCESS", "Informacion exitosa."),
    USER_CREATED_SUCCESS(201, "USER_CREATED_SUCCESS", "El usuario se creó correctamente."),
    ROLE_OPCION_GENERAL(201, "ROLE_OPCION_GENERAL", ""),
    MENU_GENERAL(200, "MENU_GENERAL", "Menu obtenido correctamente."),
    USER_UPDATED_SUCCESS(200, "USER_UPDATED_SUCCESS", "El perfil se actualizó con éxito."),
    USER_POLICY_SUCCESS(200, "USER_POLICY_SUCCESS", "Politicas recolectadas correctamente."),
    USER_UPDATED_PASS_SUCCESS(200, "USER_UPDATED_PASS_SUCCESS", "Contraseña actualizada correctamente."),
    USER_AUTH_SUCCESS(203, "USER_AUTH_SUCCESS", "Consulta exitosa."),
    MENU_AUTH_SUCCESS(204, "MENU_AUTH_SUCCESS", "Consulta exitosa."),
    SUCURSAL_DELETED_SUCCESS(200, "SUCURSAL_DELETED_SUCCESS", "La sucursal fue eliminada correctamente."),
    GENERO_SUCCESS(200, "GENERO_SUCCESS", "Genero creado Correctamente."),
    GENERO_GENERAL(200, "GENERAL_SUCCESS", "Consulta Exitosa."),
    GENERO_UPDATED_SUCCESS(200, "GENERAL_SUCCESS", "Genero actualizado Correctamente."),
    GENERO_DELETED_SUCCESS(200, "GENERAL_SUCCESS", "Genero eliminado Correctamente."),

    EMPRESA_SUCCESS(200, "EMPRESA_SUCCESS", "Empresa creada correctamente."),
    EMPRESA_GENERAL(200, "EMPRESA_GENERAL", "Consulta exitosa."),
    EMPRESA_UPDATED_SUCCESS(200, "EMPRESA_UPDATED_SUCCESS", "Empresa actualizada correctamente."),
    EMPRESA_DELETED_SUCCESS(200, "EMPRESA_DELETED_SUCCESS", "Empresa eliminada correctamente."),

    SUCURSAL_SUCCESS(200, "SUCURSAL_SUCCESS", "Sucursal creada correctamente."),
    SUCURSAL_GENERAL(200, "SUCURSAL_GENERAL", "Consulta exitosa."),
    SUCURSAL_UPDATED_SUCCESS(200, "SUCURSAL_UPDATED_SUCCESS", "Sucursal actualizada correctamente."),

    STATUS_USUARIO_SUCCESS(200, "STATUS_USUARIO_SUCCESS", "Estatus de usuario creado correctamente."),
    STATUS_USUARIO_GENERAL(200, "STATUS_USUARIO_GENERAL", "Consulta exitosa."),
    STATUS_USUARIO_UPDATED_SUCCESS(200, "STATUS_USUARIO_UPDATED_SUCCESS", "Estatus de usuario actualizado correctamente."),
    STATUS_USUARIO_DELETED_SUCCESS(200, "STATUS_USUARIO_DELETED_SUCCESS", "Estatus de usuario eliminado correctamente."),

    TIPO_ACCESO_SUCCESS(200, "TIPO_ACCESO_SUCCESS", "Tipo de acceso creado Correctamente."),
    TIPO_ACCESO_GENERAL(200, "TIPO_ACCESO_GENERAL", "Consulta Exitosa."),
    TIPO_ACCESO_UPDATED_SUCCESS(200, "TIPO_ACCESO_UPDATED_SUCCESS", "Tipo de acceso actualizado Correctamente."),
    TIPO_ACCESO_DELETED_SUCCESS(200, "TIPO_ACCESO_DELETED_SUCCESS", "Tipo de acceso eliminado Correctamente.");

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