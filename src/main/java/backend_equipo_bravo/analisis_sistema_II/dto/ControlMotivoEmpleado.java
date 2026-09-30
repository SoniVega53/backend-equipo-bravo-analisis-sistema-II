package backend_equipo_bravo.analisis_sistema_II.dto;

public enum ControlMotivoEmpleado {
    RENUNCIA(1, "Renuncia"),
    DESPIDO(2, "Despido"),
    JUBILACION(3, "Jubilación");

    private final int id;
    private final String entityName;

    ControlMotivoEmpleado(int id, String entityName) {
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
        for (ControlMotivoEmpleado opcion : values()) {
            if (opcion.getEntityName().equalsIgnoreCase(entityName)) {
                return opcion.getId();
            }
        }
        return null;
    }
}