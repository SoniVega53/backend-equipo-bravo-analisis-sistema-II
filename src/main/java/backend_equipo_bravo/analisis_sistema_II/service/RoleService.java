package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.RoleRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Role;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RoleService extends BaseService<Role, Integer> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected JpaRepository<Role, Integer> getRepository() {
        return roleRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ROLE_NOT_FOUND);
    }

    public Role crear(RoleRequest request) {
        String usuarioActual = obtenerUsuarioAutenticado();

        Role role = new Role();
        mapearDatosRequest(role, request);

        role.setUsuarioCreacion(usuarioActual);
        role.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(role);
    }

    public Role actualizar(Integer id, RoleRequest request) {
        Role existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void mapearDatosRequest(Role role, RoleRequest request) {
        role.setNombre(request.getNombre());
    }
}