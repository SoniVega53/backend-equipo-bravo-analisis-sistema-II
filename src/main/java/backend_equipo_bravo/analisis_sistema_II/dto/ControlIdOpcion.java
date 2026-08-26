package backend_equipo_bravo.analisis_sistema_II.dto;

public enum ControlIdOpcion {
    EMPRESAS(1, "Empresa"),
    SUCURSALES(2, "Sucursal"),
    GENEROS(3, "Genero"),
    ESTATUS_USUARIO(4, "StatusUsuario"),
    ROLES(5, "Role"),
    MODULOS(6, "Modulo"),
    MENUS(7, "Menu"),
    OPCIONES(8, "Opcion"),
    USUARIOS(9, "Usuario"),
    ASIG_OPCIONES(10, "RoleOpcion");

    private final int id;
    private final String entityName;

    ControlIdOpcion(int id, String entityName) {
        this.id = id;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }

    public static Integer getIdByEntityName(String entityName) {
        for (ControlIdOpcion opcion : values()) {
            if (opcion.getEntityName().equalsIgnoreCase(entityName)) {
                return opcion.getId();
            }
        }
        return null;
    }
}