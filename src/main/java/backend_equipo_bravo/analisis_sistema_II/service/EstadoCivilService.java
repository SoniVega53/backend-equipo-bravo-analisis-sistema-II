package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.EstadoCivilRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.EstadoCivil;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.EstadoCivilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EstadoCivilService extends BaseService<EstadoCivil, Integer> {

    @Autowired
    private EstadoCivilRepository estadoCivilRepository;

    @Override
    protected JpaRepository<EstadoCivil, Integer> getRepository() {
        return estadoCivilRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ESTADO_CIVIL_NOT_FOUND);
    }

    public EstadoCivil crear(EstadoCivilRequest request) {
        validar(request);
        String usuarioActual = obtenerUsuarioAutenticado();

        EstadoCivil estadoCivil = new EstadoCivil();
        mapearDatosRequest(estadoCivil, request);

        estadoCivil.setUsuarioCreacion(usuarioActual);
        estadoCivil.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(estadoCivil);
    }

    public EstadoCivil actualizar(Integer id, EstadoCivilRequest request) {
        validar(request);
        EstadoCivil existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void validar(EstadoCivilRequest request) {
        if (request.getNombre() == null
                || request.getNombre().trim().isEmpty()
                || request.getNombre().trim().length() > 50) {
            throw new BusinessException(GeneralError.NOMBRE_INVALIDO);
        }    }

    private void mapearDatosRequest(EstadoCivil estadoCivil, EstadoCivilRequest request) {
        estadoCivil.setNombre(request.getNombre().trim());
    }
}
