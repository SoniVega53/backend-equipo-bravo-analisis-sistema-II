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
    ASIG_OPCIONES(10, "RoleOpcion"),

    ESTADO_CIVIL(11, "EstadoCivil"),
    STATUS_EMPLEADO(12, "StatusEmpleado"),
    FLUJO_STATUS_EMPLEADO(13, "FlujoStatusEmpleado"),
    TIPO_DOCUMENTO(14, "TipoDocumento"),
    DEPARTAMENTO(15, "Departamento"),
    PUESTO(16, "Puesto"),
    PERSONA(17, "Persona"),
    DOCUMENTO_PERSONA(18, "DocumentoPersona"),
    BANCO(19, "Banco"),
    EMPLEADO(20, "Empleado"),
    CUENTA_BANCARIA_EMPLEADO(21, "CuentaBancariaEmpleado"),
    INASISTENCIAS(22, "Inasistencia"),
    CALCULO_PLANILLA(23, "CalculoPlanilla"),
    REPORTE_PLANILLA(24, "ReportePlanilla"),
    BOLETA_PAGO(25, "BoletaPago"),
    LIQUIDACION(26, "Liquidacion");

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