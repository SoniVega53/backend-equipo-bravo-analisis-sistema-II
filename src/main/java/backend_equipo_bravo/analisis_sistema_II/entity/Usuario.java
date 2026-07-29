package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "idusuario")
    private String idUsuario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fechanacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "idstatususuario")
    private Integer idStatusUsuario;

    @Column(name = "password")
    private String password;

    @Column(name = "idgenero")
    private Integer idGenero;

    @Column(name = "ultimafechaingreso")
    private LocalDateTime ultimaFechaIngreso;

    @Column(name = "intentosdeacceso")
    private Integer intentosDeAcceso;

    @Column(name = "sesionactual")
    private String sesionActual;

    @Column(name = "ultimafechacambiopassword")
    private LocalDateTime ultimaFechaCambioPassword;

    @Column(name = "correoelectronico")
    private String correoElectronico;

    @Column(name = "requierecambiarpassword")
    private Integer requiereCambiarPassword;

    @Column(name = "fotografia")
    private byte[] fotografia;

    @Column(name = "telefonomovil")
    private String telefonoMovil;

    @Column(name = "idsucursal")
    private Integer idSucursal;

    @Column(name = "pregunta")
    private String pregunta;

    @Column(name = "respuesta")
    private String respuesta;

    @Column(name = "idrole")
    private Integer idRole;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}