package backend_equipo_bravo.analisis_sistema_II.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilRequest {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Integer idGenero;
    private String correoElectronico;
    private String telefonoMovil;
}
