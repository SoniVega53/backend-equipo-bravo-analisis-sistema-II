package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.ModuloRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Modulo;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ModuloService extends BaseService<Modulo, Integer> {

    @Autowired
    private ModuloRepository moduloRepository;

    @Override
    protected JpaRepository<Modulo, Integer> getRepository() {
        return moduloRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.MODULO_NOT_FOUND);
    }

    public Modulo crear(ModuloRequest request) {
        String usuarioActual = obtenerUsuarioAutenticado();

        Modulo modulo = new Modulo();
        mapearDatosRequest(modulo, request);

        modulo.setUsuarioCreacion(usuarioActual);
        modulo.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(modulo);
    }

    public Modulo actualizar(Integer id, ModuloRequest request) {
        Modulo existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void mapearDatosRequest(Modulo modulo, ModuloRequest request) {
        modulo.setNombre(request.getNombre());
        modulo.setOrdenMenu(request.getOrdenMenu());
    }
}