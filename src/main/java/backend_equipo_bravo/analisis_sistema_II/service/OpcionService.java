package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.OpcionRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Opcion;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.OpcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OpcionService extends BaseService<Opcion, Integer> {

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private RoleOpcionService roleOpcionService;

    @Override
    protected JpaRepository<Opcion, Integer> getRepository() {
        return opcionRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.OPCION_NOT_FOUND);
    }

    public Opcion crear(OpcionRequest request) {
        String usuarioActual = obtenerUsuarioAutenticado();

        Opcion opcion = new Opcion();
        mapearDatosRequest(opcion, request);

        opcion.setUsuarioCreacion(usuarioActual);
        opcion.setFechaCreacion(LocalDateTime.now());

        Opcion entity = super.crearBasePermisos(opcion);

        roleOpcionService.createOpcionPermit(entity.getIdOpcion(),usuarioActual);

        return entity;
    }

    public Opcion actualizar(Integer id, OpcionRequest request) {
        Opcion existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void mapearDatosRequest(Opcion opcion, OpcionRequest request) {
        opcion.setIdMenu(request.getIdMenu());
        opcion.setNombre(request.getNombre());
        opcion.setOrdenMenu(request.getOrdenMenu());
        opcion.setPagina(request.getPagina());
    }
}