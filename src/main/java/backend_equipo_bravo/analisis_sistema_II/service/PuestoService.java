package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.PuestoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Puesto;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.PuestoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PuestoService extends BaseService<Puesto, Integer> {

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    protected JpaRepository<Puesto, Integer> getRepository() {
        return puestoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ERROR_PUESTO_NOT_FOUND);
    }

    public Puesto crear(PuestoRequest request) {
        validar(request);
        String usuarioActual = obtenerUsuarioAutenticado();

        Puesto puesto = new Puesto();
        mapearDatosRequest(puesto, request);

        puesto.setUsuarioCreacion(usuarioActual);
        puesto.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(puesto);
    }

    public Puesto actualizar(Integer id, PuestoRequest request) {
        validar(request);
        Puesto existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void validar(PuestoRequest request) {
        if (request.getNombre() == null
                || request.getNombre().trim().isEmpty()
                || request.getNombre().trim().length() > 50) {
            throw new BusinessException(GeneralError.NOMBRE_INVALIDO);
        }
        if (request.getIdDepartamento() == null
                || !departamentoRepository.existsById(request.getIdDepartamento())) {
            throw new BusinessException(GeneralError.DEPARTAMENTO_NOT_FOUND);
        }
    }

    private void mapearDatosRequest(Puesto puesto, PuestoRequest request) {
        puesto.setIdDepartamento(request.getIdDepartamento());
        puesto.setNombre(request.getNombre().trim());
    }
}
