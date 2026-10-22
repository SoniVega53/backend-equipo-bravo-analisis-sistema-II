
package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.cuentabancariaempleado.CuentaBancariaEmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.CuentaBancariaEmpleado;
import backend_equipo_bravo.analisis_sistema_II.repository.CuentaBancariaEmpleadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CuentaBancariaEmpleadoService
        extends BaseService<CuentaBancariaEmpleado, Integer> {

    @Autowired
    private CuentaBancariaEmpleadoRepository cuentaBancariaEmpleadoRepository;

    @Override
    protected JpaRepository<CuentaBancariaEmpleado, Integer> getRepository() {
        return cuentaBancariaEmpleadoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new RuntimeException(
                "No se encontró la cuenta bancaria con ID: " + id
        );
    }

    public CuentaBancariaEmpleado crear(
            CuentaBancariaEmpleadoRequest request) {

        validarRequest(request);

        String usuarioActual = obtenerUsuarioAutenticado();

        CuentaBancariaEmpleado cuenta = new CuentaBancariaEmpleado();

        cuenta.setIdEmpleado(request.getIdEmpleado());
        cuenta.setIdBanco(request.getIdBanco());
        cuenta.setNumeroDeCuenta(request.getNumeroDeCuenta().trim());
        cuenta.setActiva(request.getActiva());

        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setUsuarioCreacion(usuarioActual);

        return super.crearBasePermisos(cuenta);
    }

    public CuentaBancariaEmpleado actualizar(
            Integer id,
            CuentaBancariaEmpleadoRequest request) {

        validarRequest(request);

        CuentaBancariaEmpleado existente = super.buscarPorId(id);

        String usuarioActual = obtenerUsuarioAutenticado();

        existente.setIdEmpleado(request.getIdEmpleado());
        existente.setIdBanco(request.getIdBanco());
        existente.setNumeroDeCuenta(request.getNumeroDeCuenta().trim());
        existente.setActiva(request.getActiva());

        existente.setFechaModificacion(LocalDateTime.now());
        existente.setUsuarioModificacion(usuarioActual);

        return super.actualizarBasePermisos(existente);
    }

    private void validarRequest(CuentaBancariaEmpleadoRequest request) {

        if (request == null
                || request.getIdEmpleado() == null
                || request.getIdBanco() == null
                || request.getNumeroDeCuenta() == null
                || request.getNumeroDeCuenta().isBlank()
                || request.getActiva() == null) {

            throw new IllegalArgumentException(
                    "Debe completar todos los campos de la cuenta bancaria."
            );
        }

//        if (request.getActiva() != 'S' && request.getActiva() != 'N') {
//            throw new IllegalArgumentException(
//                    "El estado de la cuenta debe ser S o N."
//            );
//        }
    }
}
