package backend_equipo_bravo.analisis_sistema_II.service;

import org.springframework.data.jpa.repository.JpaRepository;

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
        getRepository().delete(entidad);
    }
}