package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.SelectOptionDto;
import backend_equipo_bravo.analisis_sistema_II.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogoService {
    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private StatusUsuarioRepository statusUsuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private MenuRepository menuRepository;

    public List<SelectOptionDto> getEmpresas() {
        return empresaRepository.findAll().stream()
                .map(e -> new SelectOptionDto(e.getIdEmpresa(), e.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getSucursales() {
        return sucursalRepository.findAll().stream()
                .map(s -> new SelectOptionDto(s.getIdSucursal(), s.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getSucursalesEmpresa(Integer idEmpresa) {
        return sucursalRepository.findByIdEmpresa(idEmpresa).stream()
                .map(s -> new SelectOptionDto(s.getIdSucursal(), s.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getGeneros() {
        return generoRepository.findAll().stream()
                .map(g -> new SelectOptionDto(g.getIdGenero(), g.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getStatusUsuario() {
        return statusUsuarioRepository.findAll().stream()
                .map(s -> new SelectOptionDto(s.getIdStatusUsuario(), s.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getRoles() {
        return roleRepository.findAll().stream()
                .map(r -> new SelectOptionDto(r.getIdRole(), r.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getModulos() {
        return moduloRepository.findAll().stream()
                .map(m -> new SelectOptionDto(m.getIdModulo(), m.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getMenus() {
        return menuRepository.findAll().stream()
                .map(m -> new SelectOptionDto(m.getIdMenu(), m.getNombre()))
                .collect(Collectors.toList());
    }
}