package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.usuario.*;
//import backend_equipo_bravo.analisis_sistema_II.dto.usuario.UsuarioUpdatePasswordRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.Empresa;
//import backend_equipo_bravo.analisis_sistema_II.entity.Genero;
import backend_equipo_bravo.analisis_sistema_II.entity.Sucursal;
import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.EmpresaError;
//import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.SucursalError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.EmpresaRepository;
//import backend_equipo_bravo.analisis_sistema_II.repository.GeneroRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.SucursalRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.UsuarioRepository;
import backend_equipo_bravo.analisis_sistema_II.utils.FechaUtil;
import backend_equipo_bravo.analisis_sistema_II.utils.FormatoFecha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
//import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    //@Autowired
    //private GeneroRepository generoRepository;

    @Autowired
    private PoliticasSeguridadService politicasService;

    @Autowired
    private AuditoriaService auditoriaService;

    public java.util.List<Usuario> listarUsuarios() {
    return usuarioRepository.findAll();
    }

    public Usuario buscarUsuario(String idUsuario) {

    return usuarioRepository.findByIdUsuario(idUsuario)
            .orElseThrow(() ->
                    new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));
    }

    public void eliminarUsuario(String idUsuario) {

    Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario)
            .orElseThrow(() ->
                    new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

    usuarioRepository.delete(usuario);
    }

    public Usuario crearUsuario(UsuarioCreateRequest request) {

    String usuarioActual =
            SecurityContextHolder.getContext().getAuthentication().getName();

    if (usuarioRepository.findByIdUsuario(request.getIdUsuario()).isPresent()) {
        throw new BusinessException(UsuarioError.AUTH_UNAUTHORIZED);
    }

    Usuario usuario = new Usuario();

    usuario.setIdUsuario(request.getIdUsuario());
    usuario.setNombre(request.getNombre());
    usuario.setApellido(request.getApellido());
    usuario.setFechaNacimiento(request.getFechaNacimiento());
    usuario.setCorreoElectronico(request.getCorreoElectronico());
    usuario.setTelefonoMovil(request.getTelefonoMovil());

    usuario.setIdGenero(request.getIdGenero());
    usuario.setIdRole(request.getIdRole());
    usuario.setIdStatusUsuario(request.getIdStatusUsuario());
    usuario.setIdSucursal(request.getIdSucursal());

    usuario.setPassword(
            passwordEncoder.encode(request.getPassword())
    );

    usuario.setPregunta(request.getPregunta());
    usuario.setRespuesta(request.getRespuesta());

    usuario.setIntentosDeAcceso(0);
    usuario.setRequiereCambiarPassword(1);
    usuario.setSesionActual(null);

    usuario.setFechaCreacion(LocalDateTime.now());
    usuario.setUsuarioCreacion(usuarioActual);

    return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(String idUsuarioActualizar, UsuarioUpdateRequest request) {
        String idUsuarioEjecutor = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario ejecutor = usuarioRepository.findByIdUsuario(idUsuarioEjecutor)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        boolean isAdmin = ejecutor.getIdRole() == 1;

        if (!idUsuarioEjecutor.equals(idUsuarioActualizar) && !isAdmin) {
            throw new BusinessException(UsuarioError.AUTH_UNAUTHORIZED);
        }

        boolean intentaCambiarSensibles = request.getIdRole() != null ||
                request.getIdStatusUsuario() != null ||
                request.getIntentosDeAcceso() != null;

        if (intentaCambiarSensibles && !isAdmin) {
            throw new BusinessException(UsuarioError.AUTH_UNAUTHORIZED);
        }

        Usuario usuarioDestino = usuarioRepository.findByIdUsuario(idUsuarioActualizar)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        if (request.getNombre() != null) usuarioDestino.setNombre(request.getNombre());
        if (request.getApellido() != null) usuarioDestino.setApellido(request.getApellido());
        if (request.getFechaNacimiento() != null) usuarioDestino.setFechaNacimiento(request.getFechaNacimiento());
        if (request.getCorreoElectronico() != null) usuarioDestino.setCorreoElectronico(request.getCorreoElectronico());
        if (request.getTelefonoMovil() != null) usuarioDestino.setTelefonoMovil(request.getTelefonoMovil());
        if (request.getIdSucursal() != null) usuarioDestino.setIdSucursal(request.getIdSucursal());

        if (request.getIdRole() != null) usuarioDestino.setIdRole(request.getIdRole());
        if (request.getIdStatusUsuario() != null) usuarioDestino.setIdStatusUsuario(request.getIdStatusUsuario());
        if (request.getIntentosDeAcceso() != null) usuarioDestino.setIntentosDeAcceso(request.getIntentosDeAcceso());

        // Auditoría automática
        usuarioDestino.setFechaModificacion(LocalDateTime.now());
        usuarioDestino.setUsuarioModificacion(idUsuarioEjecutor);

        return usuarioRepository.save(usuarioDestino);
    }


    public Usuario actualizarPrimerIngreso(String password) {
        String idUsuarioLog = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioLog)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        if (password == null || password.isEmpty()) {
            throw new BusinessException(UsuarioError.AUTH_PASSWORD_EMPTY);
        }

        if (usuario.getRequiereCambiarPassword() != 1) {
            throw new BusinessException(UsuarioError.AUTH_NOT_CHANGE);
        }
        if (passwordEncoder.matches(password, usuario.getPassword())) {
            throw new BusinessException(UsuarioError.AUTH_PASSWORD_EQUALS);
        }
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());
        usuario.setRequiereCambiarPassword(0);

        return usuarioRepository.save(usuario);
    }

    public int getChangePassword() {
        String idUsuarioLog = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioLog)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        boolean isReno = validarRenovacionPassword(usuario);

        if ((usuario.getUltimaFechaCambioPassword() == null ||
                (usuario.getRequiereCambiarPassword() == null || usuario.getRequiereCambiarPassword() == 1)) ||
                isReno) {
            usuario.setRequiereCambiarPassword(isReno ? 2:1);
        }else{
            usuario.setRequiereCambiarPassword(0);
        }

        usuarioRepository.save(usuario);

        return usuario.getRequiereCambiarPassword() != null ? usuario.getRequiereCambiarPassword() : 1;
    }

    private boolean validarRenovacionPassword(Usuario usuario){
        Empresa empresa = politicasService.obtenerEmpresaPorSucursal(usuario.getIdSucursal());
        Integer diasCaducidad = empresa.getPasswordCantidadCaducidadDias();

        if (diasCaducidad != null && diasCaducidad > 0 && usuario.getUltimaFechaCambioPassword() != null) {
            long diasTranscurridos = java.time.temporal.ChronoUnit.DAYS.between(
                    usuario.getUltimaFechaCambioPassword(),
                    LocalDateTime.now()
            );
            return diasTranscurridos >= diasCaducidad;
        }

        return false;
    }

    public UsuarioPerfilResponse getDataPerfil() {
        String idUsuarioLog = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioLog)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        Sucursal sucursal = sucursalRepository.findById(usuario.getIdSucursal())
                .orElseThrow(() -> new BusinessException(SucursalError.SUCURSAL_NOT_FOUND));

        Empresa empresa = empresaRepository.findById(sucursal.getIdEmpresa())
                .orElseThrow(() -> new BusinessException(EmpresaError.EMPRESA_NOT_FOUND));

        UsuarioPerfilResponse response = new UsuarioPerfilResponse();

        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setFechaNacimiento(usuario.getFechaNacimiento());
        response.setIdGenero(usuario.getIdGenero());
        response.setUltimaFechaIngreso(FechaUtil.formatear(usuario.getUltimaFechaIngreso(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
        response.setCorreoElectronico(usuario.getCorreoElectronico());
        response.setFotografia(usuario.getFotografia());
        response.setTelefonoMovil(usuario.getTelefonoMovil());
        response.setSucursal(sucursal.getNombre());
        response.setEmpresa(empresa.getNombre());
        response.setRol(usuario.getIdRole().toString());


        return response;
    }


    public Usuario actualizarPerfil(UsuarioPerfilRequest request) {
        String idUsuarioEjecutor = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioEjecutor)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));


        if (usuario == null) {
            throw new BusinessException(UsuarioError.AUTH_UNAUTHORIZED);
        }

        if (request.getNombre() != null) usuario.setNombre(request.getNombre());
        if (request.getApellido() != null) usuario.setApellido(request.getApellido());
        if (request.getFechaNacimiento() != null) usuario.setFechaNacimiento(request.getFechaNacimiento());
        if (request.getCorreoElectronico() != null) usuario.setCorreoElectronico(request.getCorreoElectronico());
        if (request.getTelefonoMovil() != null) usuario.setTelefonoMovil(request.getTelefonoMovil());
        if (request.getIdGenero() != null) usuario.setIdGenero(request.getIdGenero());


        return usuarioRepository.save(usuario);
    }

    public Usuario cambioPassword(String passwordOld, String passwordNew,String ip, String userAgent) {
        String idUsuarioLog = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioLog)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        if (passwordOld == null || passwordOld.isEmpty()) {
            throw new BusinessException(UsuarioError.AUTH_PASSWORD_EMPTY);
        }

        if (passwordNew == null || passwordNew.isEmpty()) {
            throw new BusinessException(UsuarioError.AUTH_PASSWORD_EMPTY);
        }

        if (!passwordEncoder.matches(passwordOld, usuario.getPassword())) {
            int intentosActuales = usuario.getIntentosDeAcceso() != null ? usuario.getIntentosDeAcceso() : 0;
            intentosActuales++;
            usuario.setIntentosDeAcceso(intentosActuales);

            int intentosPermitidos = politicasService.obtenerIntentosPermitidosPorSucursal(usuario.getIdSucursal());

            if (intentosActuales >= intentosPermitidos) {
                usuario.setIdStatusUsuario(2);
                usuario.setSesionActual(null);
                usuarioRepository.save(usuario);
                auditoriaService.registrarIntento(idUsuarioLog, 3, ip, userAgent, null);
                throw new BusinessException(UsuarioError.AUTH_USER_BLOCKED_ATTEMPTS);
            } else {
                usuarioRepository.save(usuario);
                auditoriaService.registrarIntento(idUsuarioLog, 2, ip, userAgent, null);
                throw new BusinessException(UsuarioError.AUTH_INVALID_CREDENTIALS_ATTEMPTS, intentosActuales, intentosPermitidos);
            }
        }

        if (passwordOld.equals(passwordNew)) {
            throw new BusinessException(UsuarioError.AUTH_PASSWORD_EQUALS);
        }
        usuario.setPassword(passwordEncoder.encode(passwordNew));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());
        usuario.setRequiereCambiarPassword(0);
        usuario.setIntentosDeAcceso(0);

        return usuarioRepository.save(usuario);
    }

    public UsuarioPreguntaResponse obtenerPregunta(
            String idUsuario
    ) {

        Usuario usuario =
                usuarioRepository.findByIdUsuario(idUsuario)
                        .orElseThrow(() ->
                                new BusinessException(
                                        UsuarioError.AUTH_USER_NOT_FOUND
                                ));

        UsuarioPreguntaResponse response =
                new UsuarioPreguntaResponse();

        response.setPregunta(usuario.getPregunta());

        return response;
    }

    public boolean validarRespuesta(
            ValidarPreguntasRequest request
    ) {

        if (
                request.getIdUsuario() == null ||
                        request.getRespuesta() == null
        ) {
            return false;
        }

        Usuario usuario =
                usuarioRepository.findByIdUsuario(
                        request.getIdUsuario().trim()
                ).orElse(null);

        if (
                usuario == null ||
                        usuario.getRespuesta() == null
        ) {
            return false;
        }

        String respuestaIngresada =
                request.getRespuesta()
                        .trim()
                        .toLowerCase();

        String respuestaGuardada =
                usuario.getRespuesta()
                        .trim()
                        .toLowerCase();

        return respuestaGuardada.equals(
                respuestaIngresada
        );
    }

    public void recuperarPassword(
            RecuperarPasswordUpdateRequest request
    ) {

        if (
                request.getIdUsuario() == null ||
                        request.getRespuesta() == null
        ) {
            throw new BusinessException(
                    UsuarioError.AUTH_USER_NOT_FOUND
            );
        }

        Usuario usuario =
                usuarioRepository.findByIdUsuario(
                        request.getIdUsuario().trim()
                ).orElseThrow(() ->
                        new BusinessException(
                                UsuarioError.AUTH_USER_NOT_FOUND
                        ));

        if (usuario.getRespuesta() == null) {
            throw new BusinessException(
                    UsuarioError.AUTH_USER_NOT_FOUND
            );
        }

        String respuestaIngresada =
                request.getRespuesta()
                        .trim()
                        .toLowerCase();

        String respuestaGuardada =
                usuario.getRespuesta()
                        .trim()
                        .toLowerCase();

        if (!respuestaGuardada.equals(respuestaIngresada)) {
            throw new BusinessException(
                    UsuarioError.AUTH_USER_NOT_FOUND
            );
        }

        if (
                request.getPassword() == null ||
                        request.getPassword().isEmpty()
        ) {
            throw new BusinessException(
                    UsuarioError.AUTH_PASSWORD_EMPTY
            );
        }

        usuario.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        usuario.setUltimaFechaCambioPassword(
                LocalDateTime.now()
        );

        usuario.setRequiereCambiarPassword(0);
        usuario.setIntentosDeAcceso(0);

        usuarioRepository.save(usuario);
    }

}