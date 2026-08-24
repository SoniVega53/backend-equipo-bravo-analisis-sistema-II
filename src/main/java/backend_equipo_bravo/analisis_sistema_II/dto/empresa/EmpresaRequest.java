package backend_equipo_bravo.analisis_sistema_II.dto.empresa;

import lombok.Data;

@Data
public class EmpresaRequest {
    private String nombre;
    private String direccion;
    private String nit;
    private Integer passwordCantidadMayusculas;
    private Integer passwordCantidadMinusculas;
    private Integer passwordCantidadCaracteresEspeciales;
    private Integer passwordCantidadCaducidadDias;
    private Integer passwordLargo;
    private Integer passwordIntentosAntesDeBloquear;
    private Integer passwordCantidadNumeros;
    private Integer passwordCantidadPreguntasValidar;
}