package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "puesto")
public class Puesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpuesto")
    private Integer idPuesto;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "iddepartamento")
    private Integer idDepartamento;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}