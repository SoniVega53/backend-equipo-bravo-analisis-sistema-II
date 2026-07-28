package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.Empresa;
import backend_equipo_bravo.analisis_sistema_II.entity.Sucursal;
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
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        Empresa empresa = empresaRepository.findById(sucursal.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        return empresa.getPasswordIntentosAntesDeBloquear() != null
                ? empresa.getPasswordIntentosAntesDeBloquear()
                : 3;
    }
}