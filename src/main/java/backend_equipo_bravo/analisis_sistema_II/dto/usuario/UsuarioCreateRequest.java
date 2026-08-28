package backend_equipo_bravo.analisis_sistema_II.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioCreateRequest {

    @NotBlank(message = "El ID de usuario es obligatorio")
    @Size(max = 50, message = "El ID de usuario no puede superar los 50 caracteres")
    private String idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    private String apellido;

    private LocalDate fechaNacimiento;

    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "El correo electronico no tiene un formato valido")
    private String correoElectronico;

    private String telefonoMovil;

    @NotNull(message = "El genero es obligatorio")
    private Integer idGenero;

    @NotNull(message = "El rol es obligatorio")
    private Integer idRole;

    @NotNull(message = "El estatus de usuario es obligatorio")
    private Integer idStatusUsuario;

    @NotNull(message = "La sucursal es obligatoria")
    private Integer idSucursal;

    @NotBlank(message = "La contrasenia es obligatoria")
    private String password;

    private String pregunta;

    private String respuesta;
}