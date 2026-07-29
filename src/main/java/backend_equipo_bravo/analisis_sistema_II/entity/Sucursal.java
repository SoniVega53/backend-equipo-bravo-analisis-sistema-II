package backend_equipo_bravo.analisis_sistema_II.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sucursal")
public class Sucursal {
    @Id
    @Column(name = "idsucursal")
    private Integer idSucursal;

    @Column(name = "idempresa")
    private Integer idEmpresa;
}