package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bitacora_acceso")
public class BitacoraAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbitacoraacceso")
    private Integer idBitacoraAcceso;

    @Column(name = "idusuario")
    private String idUsuario;

    @Column(name = "idtipoacceso")
    private Integer idTipoAcceso;

    @Column(name = "fechaacceso")
    private LocalDateTime fechaAcceso;

    @Column(name = "httpuseragent")
    private String httpUserAgent;

    @Column(name = "direccionip")
    private String direccionIp;

    @Column(name = "acceso")
    private String acceso;

    @Column(name = "sistemaoperativo")
    private String sistemaOperativo;

    @Column(name = "dispositivo")
    private String dispositivo;

    @Column(name = "browser")
    private String browser;

    @Column(name = "sesion")
    private String sesion;
}