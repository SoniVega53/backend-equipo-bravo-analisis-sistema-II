package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public abstract class BaseService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    protected abstract RuntimeException getNotFoundException(ID id);


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

    public String obtenerUsuarioAutenticado() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }else{
            throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED);
        }
    }
}