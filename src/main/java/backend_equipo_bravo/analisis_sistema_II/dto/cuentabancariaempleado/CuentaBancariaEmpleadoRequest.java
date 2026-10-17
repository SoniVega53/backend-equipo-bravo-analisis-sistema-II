
package backend_equipo_bravo.analisis_sistema_II.dto.cuentabancariaempleado;

import lombok.Data;

@Data
public class CuentaBancariaEmpleadoRequest {

    private Integer idEmpleado;

    private Integer idBanco;

    private String numeroDeCuenta;

    private Character activa;
}
