package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.tipodocumento.TipoDocumentoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.TipoDocumento;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TipoDocumentoService extends BaseService<TipoDocumento, Integer> {

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Override
    protected JpaRepository<TipoDocumento, Integer> getRepository() {
        return tipoDocumentoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    public TipoDocumento crear(TipoDocumentoRequest request) {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usuarioActual == null) {
            throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED);
        }
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setNombre(request.getNombre());
        tipoDocumento.setUsuarioCreacion(usuarioActual);
        tipoDocumento.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(tipoDocumento);
    }

    public TipoDocumento actualizar(Integer id, TipoDocumentoRequest request) {
        TipoDocumento existente = super.buscarPorId(id);

        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usuarioActual == null) {
            throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED);
        }
        existente.setNombre(request.getNombre());
        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }
}