package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.RoleRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Role;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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
    protected RuntimeException getNotFoundException(Integer integer) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    public Role crear(RoleRequest request) {

        String usuarioActual =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        if (usuarioActual == null) {
            throw new BusinessException(
                    UsuarioError.AUTH_NO_AUTHORIZED
            );
        }

        Role role = new Role();

        role.setNombre(request.getNombre());
        role.setUsuarioCreacion(usuarioActual);
        role.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(role);
    }

    public Role actualizar(
            Integer id,
            RoleRequest request
    ) {

        Role existente = super.buscarPorId(id);

        String usuarioActual =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        if (usuarioActual == null) {
            throw new BusinessException(
                    UsuarioError.AUTH_NO_AUTHORIZED
            );
        }

        existente.setNombre(request.getNombre());
        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }
}