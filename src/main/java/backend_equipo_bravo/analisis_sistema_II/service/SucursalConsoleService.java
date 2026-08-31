package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.SucursalRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Sucursal;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.SucursalError;
import backend_equipo_bravo.analisis_sistema_II.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SucursalConsoleService extends BaseService<Sucursal, Integer> {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    protected JpaRepository<Sucursal, Integer> getRepository() {
        return sucursalRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(SucursalError.SUCURSAL_NOT_FOUND);
    }

    public Sucursal crear(SucursalRequest request) {
        String usuarioActual = obtenerUsuarioAutenticado();

        Sucursal sucursal = new Sucursal();
        mapearDatosRequest(sucursal, request);

        sucursal.setUsuarioCreacion(usuarioActual);
        sucursal.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(sucursal);
    }

    public Sucursal actualizar(Integer id, SucursalRequest request) {
        Sucursal existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void mapearDatosRequest(Sucursal sucursal, SucursalRequest request) {
        sucursal.setIdEmpresa(request.getIdEmpresa());
        sucursal.setNombre(request.getNombre());
        sucursal.setDireccion(request.getDireccion());
    }
}