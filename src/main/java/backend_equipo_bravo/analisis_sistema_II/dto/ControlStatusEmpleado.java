package backend_equipo_bravo.analisis_sistema_II.dto;

public enum ControlStatusEmpleado {
    ACTIVO(1, "Activo"),
    SUSPENDIDO_IGSS(2, "Suspendido por el IGSS"),
    SUSPENDIDO_RRHH(3, "Suspendido por RRHH"),
    BAJA(4, "Baja"),
    DESPEDIDO(5, "Despedido");

    private final int id;
    private final String entityName;

    ControlStatusEmpleado(int id, String entityName) {
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
        for (ControlStatusEmpleado opcion : values()) {
            if (opcion.getEntityName().equalsIgnoreCase(entityName)) {
                return opcion.getId();
            }
        }
        return null;
    }
}