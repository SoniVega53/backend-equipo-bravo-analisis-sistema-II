package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.Data;

@Data
public class SucursalRequest {
    private Integer idEmpresa;
    private String nombre;
    private String direccion;
}
