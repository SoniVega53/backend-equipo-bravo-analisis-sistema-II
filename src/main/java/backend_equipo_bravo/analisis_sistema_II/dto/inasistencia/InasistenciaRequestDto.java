package backend_equipo_bravo.analisis_sistema_II.dto.inasistencia;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InasistenciaRequestDto {
    private Integer idEmpleado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivoInasistencia;
}