package backend_equipo_bravo.analisis_sistema_II.dto.usuario;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UsuarioDTO {

    private String idUsuario;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Integer idStatusUsuario;
    private Integer idGenero;
    private Integer intentosDeAcceso;
    private String sesionActual;
    private String correoElectronico;
    private Integer requiereCambiarPassword;
    private byte[] fotografia;
    private String telefonoMovil;
    private Integer idSucursal;
    private String pregunta;
    private String respuesta;
    private Integer idRole;
    private Integer idEmpresa;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
}