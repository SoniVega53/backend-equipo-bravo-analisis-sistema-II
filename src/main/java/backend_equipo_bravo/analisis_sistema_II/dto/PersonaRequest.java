package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonaRequest {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Integer idGenero;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private Integer idEstadoCivil;
}
