package backend_equipo_bravo.analisis_sistema_II.entity;

import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "flujo_status_empleado")
@IdClass(FlujoStatusEmpleadoId.class)
public class FlujoStatusEmpleado {

    @Id
    @Column(name = "idstatusactual")
    private Integer idStatusActual;

    @Id
    @Column(name = "idstatusnuevo")
    private Integer idStatusNuevo;

    @Column(name = "nombreevento")
    private String nombreEvento;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}