package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cuenta_bancaria_empleado")
public class CuentaBancariaEmpleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcuentabancaria")
    private Integer idCuentaBancaria;

    @Column(name = "idempleado")
    private Integer idEmpleado;

    @Column(name = "idbanco")
    private Integer idBanco;

    @Column(name = "numerodecuenta")
    private String numeroDeCuenta;

    @Column(name = "activa")
    private Character activa;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion")
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion")
    private String usuarioModificacion;
}