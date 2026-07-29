package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.genero.GeneroRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Genero;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GeneroService extends BaseService<Genero, Integer> {

    @Autowired
    private GeneroRepository generoRepository;


    @Override
    protected JpaRepository<Genero, Integer> getRepository() {
        return generoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer integer) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    public Genero crear(GeneroRequest request) {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usuarioActual == null) {
            throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED);
        }
        Genero genero = new Genero();

        genero.setNombre(request.getNombre());
        genero.setUsuarioCreacion(usuarioActual);
        genero.setFechaCreacion(LocalDateTime.now());

        return super.crearBase(genero);
    }

    public Genero actualizar(Integer id, GeneroRequest generoActualizado) {
        Genero existente = super.buscarPorId(id);

        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usuarioActual == null) {
            throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED);
        }
        existente.setNombre(generoActualizado.getNombre());

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBase(existente);
    }
}
