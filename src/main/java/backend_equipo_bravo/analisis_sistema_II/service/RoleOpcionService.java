package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.ControlIdOpcion;
import backend_equipo_bravo.analisis_sistema_II.dto.ModuloDto;
import backend_equipo_bravo.analisis_sistema_II.dto.RoleDto;
import backend_equipo_bravo.analisis_sistema_II.dto.RoleOption.*;
import backend_equipo_bravo.analisis_sistema_II.dto.genero.GeneroRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.*;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleOpcionService extends BaseService<RoleOpcion, RoleOpcionId> {
    @Autowired
    private RoleOpcionRepository roleOpcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected JpaRepository<RoleOpcion, RoleOpcionId> getRepository() {
        return roleOpcionRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(RoleOpcionId integer) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

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

    public void updateAndSave(RoleOpcionRequest request) {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usuarioActual == null) {
            throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED);
        }

        Opcion opcion = opcionRepository.findByIdOpcion(ControlIdOpcion.ASIG_OPCIONES.getId()).orElse(new Opcion());
        RoleOptionDto per = getAuthPageRole(opcion.getPagina());

        for (RoleOpcionItem item : request.getRoleOpcionItems()) {
            Optional<RoleOpcion> optData = roleOpcionRepository.findByIdRoleAndIdOpcion(item.getIdRole(), item.getIdOpcion());

            RoleOpcion data;
            if (optData.isPresent()) {
                if (!per.getCambio()) {
                    throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_MODIFY);
                }

                data = optData.get();
                data.setAlta(item.getAlta());
                data.setBaja(item.getBaja());
                data.setCambio(item.getCambio());
                data.setConsultar(item.getConsultar());
                data.setExportar(item.getExportar());
                data.setImprimir(item.getImprimir());

                data.setUsuarioModificacion(usuarioActual);
                data.setFechaModificacion(LocalDateTime.now());

                super.actualizarBase(data);
            } else {
                if (!per.getAlta()) {
                    throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_ADD);
                }
                data = new RoleOpcion();
                data.setIdRole(item.getIdRole());
                data.setIdOpcion(item.getIdOpcion());
                data.setAlta(item.getAlta());
                data.setBaja(item.getBaja());
                data.setCambio(item.getCambio());
                data.setConsultar(item.getConsultar());
                data.setExportar(item.getExportar());
                data.setImprimir(item.getImprimir());

                data.setUsuarioCreacion(usuarioActual);
                data.setFechaCreacion(LocalDateTime.now());
                super.crearBase(data);
//                roleOpcionRepository.save(data);
            }
        }
    }

    public List<RoleOpcionListadoDto> obtenerMatrizPermisos(Integer idRole, Integer idModulo) {
        try {
            String idUsuarioEjecutor = SecurityContextHolder.getContext().getAuthentication().getName();

            Usuario ejecutor = usuarioRepository.findByIdUsuario(idUsuarioEjecutor)
                    .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

            List<Menu> menus = menuRepository.findByIdModulo(idModulo);

            List<Integer> idsMenu = menus.stream()
                    .map(Menu::getIdMenu)
                    .collect(Collectors.toList());

//            List<Opcion> opcionesDelModulo = ejecutor.getIdRole() == 1 && idRole != 1 ? opcionRepository.findByIdMenuIn(idsMenu) :
//                    opcionRepository.findByIdMenuInAndIdOpcionNot(idsMenu,10);

            List<Opcion> opcionesDelModulo = opcionRepository.findByIdMenuInAndIdOpcionNot(idsMenu,ControlIdOpcion.ASIG_OPCIONES.getId());

            List<RoleOpcion> permisosExistentes = roleOpcionRepository.findByIdRole(idRole);

            Map<Integer, RoleOpcion> permisosMap = permisosExistentes.stream()
                    .collect(Collectors.toMap(RoleOpcion::getIdOpcion, permiso -> permiso));

            return opcionesDelModulo.stream().map(opcion -> {
                RoleOpcion permiso = permisosMap.get(opcion.getIdOpcion());

                return RoleOpcionListadoDto.builder()
                        .idRole(idRole)
                        .idOpcion(opcion.getIdOpcion())
                        .nombreOpcion(opcion.getNombre())
                        .consultar(permiso != null ? permiso.getConsultar() : 0)
                        .alta(permiso != null ? permiso.getAlta() : 0)
                        .baja(permiso != null ? permiso.getBaja() : 0)
                        .cambio(permiso != null ? permiso.getCambio() : 0)
                        .imprimir(permiso != null ? permiso.getImprimir() : 0)
                        .exportar(permiso != null ? permiso.getExportar() : 0)
                        .build();
            }).collect(Collectors.toList());

        } catch (Exception e) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }
    }

    public List<ModuloDto> obtenerModulos(){
        List<Modulo> modulos = moduloRepository.findAll();
        List<ModuloDto> responseModulos = new ArrayList<>();

        modulos.forEach(item -> {
            responseModulos.add(
                    ModuloDto.builder()
                            .idModulo(item.getIdModulo())
                            .nombre(item.getNombre())
                            .ordenMenu(item.getOrdenMenu())
                            .build()
            );
        });

        return responseModulos;
    }

    public List<RoleDto> obtenerRoles(){
        String idUsuarioEjecutor = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario ejecutor = usuarioRepository.findByIdUsuario(idUsuarioEjecutor)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        List<Role> roles = roleRepository.findAll();
        List<RoleDto> responseRoles = new ArrayList<>();

        roles.forEach(item -> {
            if (item.getIdRole() == 1 && ejecutor.getIdRole() != 1){
                return;
            }
            responseRoles.add(
                    RoleDto.builder()
                            .idRole(item.getIdRole())
                            .nombre(item.getNombre())
                            .build()
            );
        });

        return responseRoles;
    }
}
