package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.TipoAccesoDto;
import backend_equipo_bravo.analisis_sistema_II.entity.TipoAcceso;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.TipoAccesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoAccesoService extends BaseService<TipoAcceso, Integer> {

    private final TipoAccesoRepository repository;


    @Override
    protected JpaRepository<TipoAcceso, Integer> getRepository() {
        return this.repository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    public TipoAccesoDto crear(TipoAccesoDto dto) {
        TipoAcceso entidad = TipoAcceso.builder()
                .nombre(dto.getNombre())
                .fechaCreacion(LocalDateTime.now())
                .usuarioCreacion(obtenerUsuarioAutenticado())
                .build();

        return convertirADto(super.crearBase(entidad));
    }

    public TipoAccesoDto actualizar(Integer id, TipoAccesoDto dto) {
        TipoAcceso entidad = super.buscarPorId(id);

        entidad.setNombre(dto.getNombre());
        entidad.setFechaModificacion(LocalDateTime.now());
        entidad.setUsuarioModificacion(obtenerUsuarioAutenticado());

        return convertirADto(super.actualizarBase(entidad));
    }


    private TipoAccesoDto convertirADto(TipoAcceso entidad) {
        return TipoAccesoDto.builder()
                .idTipoAcceso(entidad.getIdTipoAcceso())
                .nombre(entidad.getNombre())
                .build();
    }
}