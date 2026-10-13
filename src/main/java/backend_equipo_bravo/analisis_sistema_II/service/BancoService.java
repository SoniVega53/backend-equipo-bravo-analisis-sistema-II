package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.BancoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Banco;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.BancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BancoService extends BaseService<Banco, Integer> {

    @Autowired
    private BancoRepository bancoRepository;

    @Override
    protected JpaRepository<Banco, Integer> getRepository() {
        return bancoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.BANCO_NOT_FOUND);
    }

    public Banco crear(BancoRequest request) {
        validar(request);
        String usuarioActual = obtenerUsuarioAutenticado();

        Banco banco = new Banco();
        mapearDatosRequest(banco, request);

        banco.setUsuarioCreacion(usuarioActual);
        banco.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(banco);
    }

    public Banco actualizar(Integer id, BancoRequest request) {
        validar(request);
        Banco existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void validar(BancoRequest request) {
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "El nombre del banco es obligatorio.");
        }
        if (request.getNombre().trim().length() > 50) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "El nombre del banco no puede superar 50 caracteres.");
        }
    }

    private void mapearDatosRequest(Banco banco, BancoRequest request) {
        banco.setNombre(request.getNombre().trim());
    }
}
