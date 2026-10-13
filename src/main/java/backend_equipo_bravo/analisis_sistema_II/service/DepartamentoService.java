package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.DepartamentoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Departamento;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.DepartamentoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepartamentoService extends BaseService<Departamento, Integer> {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    protected JpaRepository<Departamento, Integer> getRepository() {
        return departamentoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.DEPARTAMENTO_NOT_FOUND);
    }

    public Departamento crear(DepartamentoRequest request) {
        validar(request);
        String usuarioActual = obtenerUsuarioAutenticado();

        Departamento departamento = new Departamento();
        mapearDatosRequest(departamento, request);

        departamento.setUsuarioCreacion(usuarioActual);
        departamento.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(departamento);
    }

    public Departamento actualizar(Integer id, DepartamentoRequest request) {
        validar(request);
        Departamento existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void validar(DepartamentoRequest request) {
        if (request.getNombre() == null
                || request.getNombre().trim().isEmpty()
                || request.getNombre().trim().length() > 50) {
            throw new BusinessException(GeneralError.NOMBRE_INVALIDO);
        }
        if (request.getIdEmpresa() == null
                || !empresaRepository.existsById(request.getIdEmpresa())) {
            throw new BusinessException(GeneralError.EMPRESA_NOT_FOUND_CATALOGO);
        }
    }

    private void mapearDatosRequest(Departamento departamento, DepartamentoRequest request) {
        departamento.setIdEmpresa(request.getIdEmpresa());
        departamento.setNombre(request.getNombre().trim());
    }
}
