package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.MenuItemDto;
import backend_equipo_bravo.analisis_sistema_II.entity.*;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private RoleOpcionRepository roleOpcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<MenuItemDto> getMenuCompleto() {
        try {
            String idUsuarioLogg = SecurityContextHolder.getContext().getAuthentication().getName();

            Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioLogg)
                    .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

            List<RoleOpcion> permisosRol = roleOpcionRepository.findByIdRole(usuario.getIdRole());

            Set<Integer> opcionesPermitidasIds = permisosRol.stream()
                    .filter(permiso -> permiso.getConsultar() == 1 ||
                            permiso.getAlta() == 1 ||
                            permiso.getBaja() == 1 ||
                            permiso.getCambio() == 1)
                    .map(RoleOpcion::getIdOpcion)
                    .collect(Collectors.toSet());

            List<Modulo> modulos = moduloRepository.findAll();
            List<Menu> menus = menuRepository.findAll();
            List<Opcion> opcionesTodas = opcionRepository.findAll();

            List<Opcion> opcionesFiltradas = opcionesTodas.stream()
                    .filter(opcion -> opcionesPermitidasIds.contains(opcion.getIdOpcion()))
                    .collect(Collectors.toList());

            Map<Integer, List<Opcion>> opcionesPorMenu = opcionesFiltradas.stream()
                    .collect(Collectors.groupingBy(Opcion::getIdMenu));

            Map<Integer, List<Menu>> menusPorModulo = menus.stream()
                    .collect(Collectors.groupingBy(Menu::getIdModulo));

            return modulos.stream()
                    .map(modulo -> buildModuloDto(modulo, menusPorModulo, opcionesPorMenu))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }
    }

    private MenuItemDto buildModuloDto(Modulo modulo, Map<Integer, List<Menu>> menusPorModulo, Map<Integer, List<Opcion>> opcionesPorMenu) {
        List<MenuItemDto> menuChildren = menusPorModulo.getOrDefault(modulo.getIdModulo(), List.of())
                .stream()
                .map(menu -> buildMenuDto(menu, opcionesPorMenu))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (menuChildren.isEmpty()) {
            return null;
        }

        return MenuItemDto.builder()
                .id("modulo-".concat(String.valueOf(modulo.getIdModulo().longValue())))
                .label(modulo.getNombre())
                .expanded(true)
                .children(menuChildren)
                .build();
    }

    private MenuItemDto buildMenuDto(Menu menu, Map<Integer, List<Opcion>> opcionesPorMenu) {
        List<MenuItemDto> opcionChildren = opcionesPorMenu.getOrDefault(menu.getIdMenu(), List.of())
                .stream()
                .map(this::buildOpcionDto)
                .collect(Collectors.toList());

        if (opcionChildren.isEmpty()) {
            return null;
        }

        return MenuItemDto.builder()
                .id("menu-".concat(String.valueOf(menu.getIdMenu().longValue())))
                .label(menu.getNombre())
                .expanded(true)
                .children(opcionChildren)
                .build();
    }

    private MenuItemDto buildOpcionDto(Opcion opcion) {
        return MenuItemDto.builder()
                .id("opcion-".concat(String.valueOf(opcion.getIdOpcion().longValue())))
                .label(opcion.getNombre())
                .url(opcion.getPagina())
                .build();
    }
}