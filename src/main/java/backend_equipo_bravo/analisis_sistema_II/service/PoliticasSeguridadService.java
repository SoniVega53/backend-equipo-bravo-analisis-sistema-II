package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.Empresa;
import backend_equipo_bravo.analisis_sistema_II.entity.Sucursal;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.EmpresaError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.SucursalError;
import backend_equipo_bravo.analisis_sistema_II.repository.EmpresaRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PoliticasSeguridadService {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public int obtenerIntentosPermitidosPorSucursal(Integer idSucursal) {
        Sucursal sucursal = sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new BusinessException(SucursalError.SUCURSAL_NOT_FOUND));

        Empresa empresa = empresaRepository.findById(sucursal.getIdEmpresa())
                .orElseThrow(() -> new BusinessException(EmpresaError.EMPRESA_NOT_FOUND));

        return empresa.getPasswordIntentosAntesDeBloquear() != null
                ? empresa.getPasswordIntentosAntesDeBloquear()
                : 3;
    }
}