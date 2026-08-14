package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.MenuItemDto;
import backend_equipo_bravo.analisis_sistema_II.dto.RoleOptionDto;
import backend_equipo_bravo.analisis_sistema_II.entity.*;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.OpcionRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.RoleOpcionRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RoleOpcionService {
    @Autowired
    private RoleOpcionRepository roleOpcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OpcionRepository opcionRepository;


    public RoleOptionDto getAuthPageRole(String pagina) {
        try {
            String idUsuarioLogg = SecurityContextHolder.getContext().getAuthentication().getName();

            Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioLogg)
                    .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

            Opcion opcion = opcionRepository.findByPagina(pagina).orElse(new Opcion());


            RoleOpcion permisoRol = roleOpcionRepository.findByIdRoleAndIdOpcion(usuario.getIdRole(), opcion.getIdOpcion())
                    .orElse(new RoleOpcion());

            RoleOptionDto role = RoleOptionDto.builder()
                    .consultar(permisoRol.getConsultar() == 1)
                    .alta(permisoRol.getAlta() == 1)
                    .baja(permisoRol.getBaja() == 1)
                    .cambio(permisoRol.getCambio() == 1)
                    .imprimir(permisoRol.getImprimir() == 1)
                    .exportar(permisoRol.getExportar() == 1)
                    .build();

        return role;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }
    }
}
