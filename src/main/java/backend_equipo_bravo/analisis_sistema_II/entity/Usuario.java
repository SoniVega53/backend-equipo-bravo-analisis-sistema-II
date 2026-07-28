package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "idusuario")
    private String idUsuario;

    @Column(name = "password")
    private String password;

    @Column(name = "idstatususuario")
    private Integer idStatusUsuario;

    @Column(name = "intentosdeacceso")
    private Integer intentosDeAcceso;

    @Column(name = "idsucursal")
    private Integer idSucursal;

    @Column(name = "idrole")
    private Integer idRole;

    @Column(name = "ultimafechaingreso")
    private LocalDateTime ultimaFechaIngreso;

    @Column(name = "sesionactual")
    private String sesionActual;
}