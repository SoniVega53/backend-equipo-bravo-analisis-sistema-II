package backend_equipo_bravo.analisis_sistema_II.dto.empresa;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmpresaRequestDto {
    private Integer idEmpresa;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El NIT es obligatorio")
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