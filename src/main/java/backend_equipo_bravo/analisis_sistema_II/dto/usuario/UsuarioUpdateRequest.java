package backend_equipo_bravo.analisis_sistema_II.dto.usuario;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioUpdateRequest {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String correoElectronico;
    private String telefonoMovil;
    private Integer idSucursal;

    private Integer idRole;
    private Integer idStatusUsuario;
    private Integer intentosDeAcceso;
}
