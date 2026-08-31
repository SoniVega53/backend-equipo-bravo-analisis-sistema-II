package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.empresa.EmpresaRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Empresa;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.EmpresaError;
import backend_equipo_bravo.analisis_sistema_II.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmpresaConsoleService extends BaseService<Empresa, Integer> {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    protected JpaRepository<Empresa, Integer> getRepository() {
        return empresaRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(EmpresaError.EMPRESA_NOT_FOUND);
    }

    public Empresa crear(EmpresaRequest request) {
        String usuarioActual = obtenerUsuarioAutenticado();

        Empresa empresa = new Empresa();
        mapearDatosRequest(empresa, request);

        empresa.setUsuarioCreacion(usuarioActual);
        empresa.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(empresa);
    }

    public Empresa actualizar(Integer id, EmpresaRequest request) {
        Empresa existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void mapearDatosRequest(Empresa empresa, EmpresaRequest request) {
        empresa.setNombre(request.getNombre());
        empresa.setDireccion(request.getDireccion());
        empresa.setNit(request.getNit());
        empresa.setPasswordCantidadMayusculas(request.getPasswordCantidadMayusculas());
        empresa.setPasswordCantidadMinusculas(request.getPasswordCantidadMinusculas());
        empresa.setPasswordCantidadCaracteresEspeciales(request.getPasswordCantidadCaracteresEspeciales());
        empresa.setPasswordCantidadCaducidadDias(request.getPasswordCantidadCaducidadDias());
        empresa.setPasswordLargo(request.getPasswordLargo());
        empresa.setPasswordIntentosAntesDeBloquear(request.getPasswordIntentosAntesDeBloquear());
        empresa.setPasswordCantidadNumeros(request.getPasswordCantidadNumeros());
        empresa.setPasswordCantidadPreguntasValidar(request.getPasswordCantidadPreguntasValidar());
        //empresa.setPasswordCantidadPreguntasValidar(1);
    }
}