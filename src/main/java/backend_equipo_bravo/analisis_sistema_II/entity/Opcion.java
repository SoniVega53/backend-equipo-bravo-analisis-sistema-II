package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "opcion")
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idopcion")
    private Integer idOpcion;

    @Column(name = "idmenu")
    private Integer idMenu;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ordenmenu")
    private Integer ordenMenu;

    @Column(name = "pagina")
    private String pagina;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;

    public Opcion() {
        idOpcion = -1;
    }
}