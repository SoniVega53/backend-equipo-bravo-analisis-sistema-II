package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// Si Empresa está en otro paquete, agrégalo aquí. De lo contrario, asegúrate que Empresa.java tenga 'package backend_equipo_bravo.analisis_sistema_II.entity;'
import backend_equipo_bravo.analisis_sistema_II.entity.Empresa;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SUCURSAL", schema = "ProyectoAnalisis")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSucursal")
    private Integer idSucursal;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "Direccion", nullable = false)
    private String direccion;

    @Column(name = "IdEmpresa", nullable = false, insertable = false, updatable = false)
    private Integer idEmpresa;

    @ManyToOne
    @JoinColumn(name = "IdEmpresa", nullable = false)
    private Empresa empresa;

    @Column(name = "FechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion", nullable = false)
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}