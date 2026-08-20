package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.ControlIdOpcion;
import backend_equipo_bravo.analisis_sistema_II.dto.RoleOption.RoleOptionDto;
import backend_equipo_bravo.analisis_sistema_II.entity.Opcion;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.OpcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    protected abstract RuntimeException getNotFoundException(ID id);

    @Autowired
    @Lazy
    private OpcionRepository opcionRepository;

    @Autowired
    @Lazy
    private RoleOpcionService seguridadService;

    private String getEntityName() {
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        return entityClass.getSimpleName();
    }

    protected Integer getIdOpcion() {
        return ControlIdOpcion.getIdByEntityName(getEntityName());
    }

    private RoleOptionDto obtenerPermisos() {
        Integer idOpcion = getIdOpcion();

        if (idOpcion == null) {
            return null;
        }

        Opcion opcion = opcionRepository.findById(idOpcion)
                .orElseThrow(() -> new BusinessException(GeneralError.ERROR_SERVICE));

        return seguridadService.getAuthPageRole(opcion.getPagina());
    }

    protected void validarConsulta() {
        RoleOptionDto per = obtenerPermisos();
        if (per != null && !per.getConsultar()) throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_VIEW);
    }

    protected void validarAlta() {
        RoleOptionDto per = obtenerPermisos();
        if (per != null && !per.getAlta()) throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_ADD);
    }

    protected void validarCambio() {
        RoleOptionDto per = obtenerPermisos();
        if (per != null && !per.getCambio()) throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_MODIFY);
    }

    protected void validarBaja() {
        RoleOptionDto per = obtenerPermisos();
        if (per != null && !per.getBaja()) throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_DELETE);
    }

    public List<T> buscarTodos() {
        validarConsulta();
        return getRepository().findAll();
    }

    public T buscarPorId(ID id) {
        validarConsulta();
        return getRepository().findById(id)
                .orElseThrow(() -> getNotFoundException(id));
    }

    protected T crearBase(T entidad) {
        validarAlta();
        return getRepository().save(entidad);
    }

    protected T actualizarBase(T entidad) {
        validarCambio();
        return getRepository().save(entidad);
    }

    public void eliminarBase(ID id) {
        validarBaja();
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

    public String obtenerUsuarioAutenticado() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } else {
            throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED);
        }
    }
}