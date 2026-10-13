package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.PersonaRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Persona;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.EstadoCivilRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.GeneroRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class PersonaService extends BaseService<Persona, Integer> {

    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^[0-9+()\\- ]{7,50}$");

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private EstadoCivilRepository estadoCivilRepository;

    @Override
    protected JpaRepository<Persona, Integer> getRepository() {
        return personaRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ERROR_PERSONA_NOT_FOUND);
    }

    public Persona crear(PersonaRequest request) {
        validar(request);
        String usuarioActual = obtenerUsuarioAutenticado();

        Persona persona = new Persona();
        mapearDatosRequest(persona, request);

        persona.setUsuarioCreacion(usuarioActual);
        persona.setFechaCreacion(LocalDateTime.now());

        return super.crearBasePermisos(persona);
    }

    public Persona actualizar(Integer id, PersonaRequest request) {
        validar(request);
        Persona existente = super.buscarPorId(id);
        String usuarioActual = obtenerUsuarioAutenticado();

        mapearDatosRequest(existente, request);

        existente.setUsuarioModificacion(usuarioActual);
        existente.setFechaModificacion(LocalDateTime.now());

        return super.actualizarBasePermisos(existente);
    }

    private void validar(PersonaRequest r) {
        textoObligatorio(r.getNombre(), "El nombre", 50);
        textoObligatorio(r.getApellido(), "El apellido", 50);
        textoObligatorio(r.getDireccion(), "La dirección", 100);
        textoObligatorio(r.getTelefono(), "El teléfono", 50);

        if (!PATRON_TELEFONO.matcher(r.getTelefono().trim()).matches()) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS,
                    "El teléfono solo puede contener números, espacios, +, - y paréntesis (mínimo 7 caracteres).");
        }

        if (r.getCorreoElectronico() != null && !r.getCorreoElectronico().trim().isEmpty()) {
            String correo = r.getCorreoElectronico().trim();
            if (correo.length() > 50) {
                throw new BusinessException(GeneralError.VALIDACION_DATOS, "El correo electrónico no puede superar 50 caracteres.");
            }
            if (!PATRON_CORREO.matcher(correo).matches()) {
                throw new BusinessException(GeneralError.VALIDACION_DATOS, "El correo electrónico no tiene un formato válido.");
            }
        }

        if (r.getFechaNacimiento() == null) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "La fecha de nacimiento es obligatoria.");
        }
        if (r.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "La fecha de nacimiento no puede ser futura.");
        }
        if (r.getFechaNacimiento().isBefore(LocalDate.now().minusYears(120))) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "La fecha de nacimiento no es válida.");
        }

        if (r.getIdGenero() == null || !generoRepository.existsById(r.getIdGenero())) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "El género seleccionado no existe.");
        }
        if (r.getIdEstadoCivil() == null || !estadoCivilRepository.existsById(r.getIdEstadoCivil())) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, "El estado civil seleccionado no existe.");
        }
    }

    private void textoObligatorio(String valor, String campo, int max) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, campo + " es obligatorio.");
        }
        if (valor.trim().length() > max) {
            throw new BusinessException(GeneralError.VALIDACION_DATOS, campo + " no puede superar " + max + " caracteres.");
        }
    }

    private void mapearDatosRequest(Persona persona, PersonaRequest r) {
        persona.setNombre(r.getNombre().trim());
        persona.setApellido(r.getApellido().trim());
        persona.setFechaNacimiento(r.getFechaNacimiento());
        persona.setIdGenero(r.getIdGenero());
        persona.setDireccion(r.getDireccion().trim());
        persona.setTelefono(r.getTelefono().trim());
        String correo = r.getCorreoElectronico();
        persona.setCorreoElectronico(correo == null || correo.trim().isEmpty() ? null : correo.trim());
        persona.setIdEstadoCivil(r.getIdEstadoCivil());
    }
}
