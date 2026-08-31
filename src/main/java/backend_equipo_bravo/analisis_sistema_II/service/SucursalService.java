package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.Empresa;
import backend_equipo_bravo.analisis_sistema_II.entity.Sucursal;
import backend_equipo_bravo.analisis_sistema_II.repository.EmpresaRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Sucursal> listarTodas() {
        return sucursalRepository.findAll();
    }

    public Optional<Sucursal> obtenerPorId(Integer id) {
        return sucursalRepository.findById(id);
    }

    public Sucursal guardar(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    public Sucursal actualizar(Integer id, Sucursal sucursalDetalles) {
        return sucursalRepository.findById(id).map(sucursal -> {
            sucursal.setNombre(sucursalDetalles.getNombre());
            sucursal.setDireccion(sucursalDetalles.getDireccion());
            sucursal.setIdEmpresa(sucursalDetalles.getIdEmpresa());

            return sucursalRepository.save(sucursal);
        }).orElseThrow(() -> new RuntimeException("Sucursal no encontrada con id: " + id));
    }

    public void eliminar(Integer id) {
        sucursalRepository.deleteById(id);
    }
}