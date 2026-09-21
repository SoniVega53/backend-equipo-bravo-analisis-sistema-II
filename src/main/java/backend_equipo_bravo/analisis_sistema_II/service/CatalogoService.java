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

    @Autowired
    private EstadoCivilRepository estadoCivilRepository;

    @Autowired
    private StatusEmpleadoRepository statusEmpleadoRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PeriodoPlanillaRepository periodoPlanillaRepository;

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

    public List<SelectOptionDto> getMenus(Integer idModulo) {
        return menuRepository.findByIdModulo(idModulo).stream()
                .map(m -> new SelectOptionDto(m.getIdMenu(), m.getNombre()))
                .collect(Collectors.toList());
    }



    public List<SelectOptionDto> getEstadosCiviles() {
        return estadoCivilRepository.findAll().stream()
                .map(e -> new SelectOptionDto(e.getIdEstadoCivil(), e.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getStatusEmpleados() {
        return statusEmpleadoRepository.findAll().stream()
                .map(s -> new SelectOptionDto(s.getIdStatusEmpleado(), s.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getTiposDocumentos() {
        return tipoDocumentoRepository.findAll().stream()
                .map(t -> new SelectOptionDto(t.getIdTipoDocumento(), t.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getDepartamentos() {
        return departamentoRepository.findAll().stream()
                .map(d -> new SelectOptionDto(d.getIdDepartamento(), d.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getPuestos() {
        return puestoRepository.findAll().stream()
                .map(p -> new SelectOptionDto(p.getIdPuesto(), p.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getPuestosDepartamento(Integer idDepartamento) {
        return puestoRepository.findAll().stream()
                .filter(p -> p.getIdDepartamento() != null && p.getIdDepartamento().equals(idDepartamento))
                .map(p -> new SelectOptionDto(p.getIdPuesto(), p.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getPersonas() {
        return personaRepository.findAll().stream()
                .map(p -> new SelectOptionDto(p.getIdPersona(), p.getNombre() + " " + p.getApellido()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getBancos() {
        return bancoRepository.findAll().stream()
                .map(b -> new SelectOptionDto(b.getIdBanco(), b.getNombre()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(e -> new SelectOptionDto(e.getIdEmpleado(), "Empleado ID: " + e.getIdEmpleado()))
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getPeriodosPlanilla() {
        return periodoPlanillaRepository.findAll().stream()
                .map(p -> new SelectOptionDto(p.getAnio(), "Año: " + p.getAnio() + " - Mes: " + p.getMes()))
                .collect(Collectors.toList());
    }
}