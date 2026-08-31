package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.ControlIdOpcion;
import backend_equipo_bravo.analisis_sistema_II.dto.RoleOption.RoleOptionDto;
import backend_equipo_bravo.analisis_sistema_II.entity.Opcion;
import backend_equipo_bravo.analisis_sistema_II.entity.RoleOpcion;
import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.RoleOpcionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseService<T, ID> {

    @Autowired
    @Lazy
    private JwtService jwtService;

    @Autowired
    @Lazy
    private RoleOpcionRepository roleOpcionRepository;

    protected abstract JpaRepository<T, ID> getRepository();

    protected abstract RuntimeException getNotFoundException(ID id);

    private String getEntityName() {
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        return entityClass.getSimpleName();
    }

    protected Integer getIdOpcion() {
        return ControlIdOpcion.getIdByEntityName(getEntityName());
    }

    private RoleOpcion obtenerPermisos(){
        RoleOpcion permisoRol = roleOpcionRepository
                .findByIdRoleAndIdOpcion(
                        obtenerRolAutenticado(),
                        getIdOpcion()
                )
                .orElse(new RoleOpcion());
        return permisoRol;
    }

    public List<T> buscarTodos() {
        return getRepository().findAll();
    }

    public T buscarPorId(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> getNotFoundException(id));
    }

    protected T crearBase(T entidad) {
        return getRepository().save(entidad);
    }

    protected T actualizarBase(T entidad) {
        return getRepository().save(entidad);
    }

    public void eliminarBase(ID id) {
        T entidad = buscarPorId(id);

        try {
            getRepository().delete(entidad);
            getRepository().flush();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(GeneralError.ERROR_DEPENDENCY);
        } catch (Exception e) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }
    }

    public String eliminarBasePermisos(ID id) {
        validarBaja();
        eliminarBase(id);
        return "Eliminado Correctamente";
    }

    public List<T> buscarTodosPermisos() {
        validarConsulta();
        return buscarTodos();
    }

    public T buscarPorIdPermisos(ID id) {
        validarConsulta();
        return buscarPorId(id);
    }

    protected T crearBasePermisos(T entidad) {
        validarAlta();
        return crearBase(entidad);
    }

    protected T actualizarBasePermisos(T entidad) {
        validarCambio();
        return actualizarBase(entidad);
    }

    protected void validarConsulta() {
        RoleOpcion per = obtenerPermisos();
        if (per != null && obtenerPermisos().getConsultar() == 0) throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_VIEW);
    }

    protected void validarAlta() {
        RoleOpcion per = obtenerPermisos();
        if (per != null && obtenerPermisos().getAlta() == 0) throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_ADD);
    }

    protected void validarCambio() {
        RoleOpcion per = obtenerPermisos();
        if (per != null && obtenerPermisos().getCambio() == 0) throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_MODIFY);
    }

    protected void validarBaja() {
        RoleOpcion per = obtenerPermisos();
        if (per != null && obtenerPermisos().getBaja() == 0) throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_DELETE);
    }

    public String obtenerUsuarioAutenticado() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }else{
            throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED);
        }
    }

    public Integer obtenerRolAutenticado() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.obtenerRolDelToken(token);
        }

        throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED);
    }

}