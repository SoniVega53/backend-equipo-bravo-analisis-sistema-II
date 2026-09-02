package backend_equipo_bravo.analisis_sistema_II.dto;

public enum ControlRolUser {
    Administrador(1, "Administrador"),
    SinOpciones(2, "Sin Opciones");

    private final int id;
    private final String entityName;

    ControlRolUser(int id, String entityName) {
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
        for (ControlRolUser opcion : values()) {
            if (opcion.getEntityName().equalsIgnoreCase(entityName)) {
                return opcion.getId();
            }
        }
        return null;
    }
}