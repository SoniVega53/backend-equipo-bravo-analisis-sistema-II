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
public class EmpresaService extends BaseService<Empresa, Integer> {

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

        Empresa empresa = new Empresa();

        empresa.setNombre(request.getNombre());
        empresa.setDireccion(request.getDireccion());
        empresa.setNit(request.getNit());

        empresa.setUsuarioCreacion(obtenerUsuarioAutenticado());
        empresa.setFechaCreacion(LocalDateTime.now());

        return super.crearBase(empresa);
    }

    public Empresa actualizar(Integer id, EmpresaRequest request) {

        Empresa empresa = super.buscarPorId(id);

        empresa.setNombre(request.getNombre());
        empresa.setDireccion(request.getDireccion());
        empresa.setNit(request.getNit());

        empresa.setUsuarioModificacion(obtenerUsuarioAutenticado());
        empresa.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBase(empresa);
    }
}