package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.periodo_planilla.PeriodoPlanillaDto;
import backend_equipo_bravo.analisis_sistema_II.entity.PeriodoPlanilla;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.PeriodoPlanillaId;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.PeriodoPlanillaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PeriodoPlanillaService extends BaseService<PeriodoPlanilla, PeriodoPlanillaId> {

    @Autowired
    private PeriodoPlanillaRepository periodoPlanillaRepository;

    @Override
    protected JpaRepository<PeriodoPlanilla, PeriodoPlanillaId> getRepository() {
        return periodoPlanillaRepository;
    }

    public PeriodoPlanillaService() {
        setEntityName("CalculoPlanilla");
    }

    @Override
    protected RuntimeException getNotFoundException(PeriodoPlanillaId id) {
        return new RuntimeException("Periodo de planilla no encontrado para el año " + id.getAnio() + " y mes " + id.getMes());
    }

    public List<PeriodoPlanillaDto> obtenerTodos() {
        return buscarTodosPermisos().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    public PeriodoPlanillaDto obtenerPorId(Integer anio, Integer mes) {
        PeriodoPlanillaId id = new PeriodoPlanillaId(anio, mes);
        return mapearADto(buscarPorIdPermisos(id));
    }

    public PeriodoPlanillaDto guardar(PeriodoPlanillaDto dto) {
        PeriodoPlanillaId id = new PeriodoPlanillaId(dto.getAnio(), dto.getMes());
        if (periodoPlanillaRepository.existsById(id)) {
            throw new BusinessException(GeneralError.PERIODO_PLANILLA_ALREADY_EXISTS);
        }

        PeriodoPlanilla entity = new PeriodoPlanilla();
        entity.setAnio(dto.getAnio());
        entity.setMes(dto.getMes());
        entity.setFechaInicio(dto.getFechaInicio());
        entity.setFechaFin(dto.getFechaFin());
        entity.setFechaCreacion(LocalDateTime.now());
        entity.setUsuarioCreacion(obtenerUsuarioAutenticado()); 

        PeriodoPlanilla saved = crearBasePermisos(entity);
        return mapearADto(saved);
    }

    public PeriodoPlanillaDto actualizar(Integer anio, Integer mes, PeriodoPlanillaDto dto) {
        PeriodoPlanillaId id = new PeriodoPlanillaId(anio, mes);
        PeriodoPlanilla entity = buscarPorIdPermisos(id);

        entity.setFechaInicio(dto.getFechaInicio());
        entity.setFechaFin(dto.getFechaFin());
        entity.setFechaModificacion(LocalDateTime.now());
        entity.setUsuarioModificacion(obtenerUsuarioAutenticado());

        PeriodoPlanilla updated = actualizarBasePermisos(entity);
        return mapearADto(updated);
    }

    public void eliminar(Integer anio, Integer mes) {
        PeriodoPlanillaId id = new PeriodoPlanillaId(anio, mes);
        eliminarBasePermisos(id);
    }

    private PeriodoPlanillaDto mapearADto(PeriodoPlanilla entity) {
        PeriodoPlanillaDto dto = new PeriodoPlanillaDto();
        dto.setAnio(entity.getAnio());
        dto.setMes(entity.getMes());
        dto.setFechaInicio(entity.getFechaInicio());
        dto.setFechaFin(entity.getFechaFin());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setUsuarioCreacion(entity.getUsuarioCreacion());
        return dto;
    }
}
