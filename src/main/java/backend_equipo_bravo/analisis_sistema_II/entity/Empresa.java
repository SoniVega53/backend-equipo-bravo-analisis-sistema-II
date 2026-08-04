package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "empresa")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idempresa")
    private Integer idEmpresa;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "nit", nullable = false, length = 20)
    private String nit;

    @Column(name = "passwordcantidadmayusculas")
    private Integer passwordCantidadMayusculas;

    @Column(name = "passwordcantidadminusculas")
    private Integer passwordCantidadMinusculas;

    @Column(name = "passwordcantidadcaracteresespeciales")
    private Integer passwordCantidadCaracteresEspeciales;

    @Column(name = "passwordcantidadcaducidaddias")
    private Integer passwordCantidadCaducidadDias;

    @Column(name = "passwordlargo")
    private Integer passwordLargo;

    @Column(name = "passwordintentosantesdebloquear")
    private Integer passwordIntentosAntesDeBloquear;

    @Column(name = "passwordcantidadnumeros")
    private Integer passwordCantidadNumeros;

    @Column(name = "passwordcantidadpreguntasvalidar")
    private Integer passwordCantidadPreguntasValidar;

    @Column(name = "fechacreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "usuariocreacion", nullable = false, length = 100, updatable = false)
    private String usuarioCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuariomodificacion", length = 100)
    private String usuarioModificacion;
}