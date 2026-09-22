package backend_equipo_bravo.analisis_sistema_II.dto.inasistencia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class InasistenciaResponseDto {
    private Integer idInasistencia;
    private Integer idEmpleado;
    private String nombreEmpleado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivoInasistencia;
    private LocalDate fechaProcesado;
    private boolean procesado;
    private LocalDateTime fechaCreacion;
    private String usuarioCreacio;

}