package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.MenuRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Menu;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class MenuConsoleService extends BaseService<Menu, Integer> {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    protected JpaRepository<Menu, Integer> getRepository() {
        return menuRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.MENU_NOT_FOUND);
    }

    public Menu crear(MenuRequest request) {
        String usuarioActual = obtenerUsuarioAutenticado();

        Menu menu = new Menu();
        mapearDatosRequest(menu, request);

        menu.setUsuarioCreacion(usuarioActual);
        menu.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(menu);
    }

    public Menu actualizar(Integer id, MenuRequest request) {
        Menu existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void mapearDatosRequest(Menu menu, MenuRequest request) {
        menu.setIdModulo(request.getIdModulo());
        menu.setNombre(request.getNombre());
        menu.setOrdenMenu(request.getOrdenMenu());
    }
}
