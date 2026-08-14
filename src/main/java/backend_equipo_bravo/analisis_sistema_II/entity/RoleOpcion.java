package backend_equipo_bravo.analisis_sistema_II.entity;

import backend_equipo_bravo.analisis_sistema_II.dto.RoleOpcionId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "role_opcion")
@IdClass(RoleOpcionId.class)
public class RoleOpcion {

    @Id
    @Column(name = "idrole")
    private Integer idRole;

    @Id
    @Column(name = "idopcion")
    private Integer idOpcion;

    @Column(name = "consultar")
    private Integer consultar;

    @Column(name = "alta")
    private Integer alta;

    @Column(name = "baja")
    private Integer baja;

    @Column(name = "cambio")
    private Integer cambio;

    @Column(name = "imprimir")
    private Integer imprimir;

    @Column(name = "exportar")
    private Integer exportar;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;

    public RoleOpcion() {
        this.consultar = 0;
        this.alta = 0;
        this.baja = 0;
        this.cambio = 0;
        this.imprimir = 0;
        this.exportar = 0;
    }


}