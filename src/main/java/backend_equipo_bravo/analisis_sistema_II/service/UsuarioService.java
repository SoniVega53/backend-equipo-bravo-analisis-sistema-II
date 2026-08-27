package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.ControlIdOpcion;
import backend_equipo_bravo.analisis_sistema_II.dto.usuario.*;
import backend_equipo_bravo.analisis_sistema_II.entity.*;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.EmpresaError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.SucursalError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.*;
import backend_equipo_bravo.analisis_sistema_II.utils.FechaUtil;
import backend_equipo_bravo.analisis_sistema_II.utils.FormatoFecha;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

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

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private PoliticasSeguridadService politicasService;

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private RoleOpcionRepository roleOpcionRepository;

    public void eliminarUsuario(String idUsuario){
        String idUsuarioLogg = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        if (idUsuarioLogg.equalsIgnoreCase(idUsuario)){
            throw new BusinessException(
                    GeneralError.ERROR_SERVICE
            );
        }

        Usuario ejecutor = usuarioRepository.findByIdUsuario(idUsuarioLogg)
                .orElseThrow(() -> new BusinessException(
                        UsuarioError.AUTH_USER_NOT_FOUND
                ));

        RoleOpcion permisoRol = roleOpcionRepository
                .findByIdRoleAndIdOpcion(
                        ejecutor.getIdRole(),
                        ControlIdOpcion.USUARIOS.getId()
                )
                .orElse(new RoleOpcion());

        if (permisoRol.getBaja() == 0) {
            throw new BusinessException(
                    UsuarioError.AUTH_NO_AUTHORIZED_DELETE
            );
        }
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new BusinessException(
                        UsuarioError.AUTH_USER_NOT_FOUND
                ));
        try {
            usuarioRepository.delete(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(GeneralError.ERROR_DEPENDENCY);
        } catch (Exception e) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }
    }

    public List<UsuarioDTO> buscarTodos() {

        String idUsuarioLogg = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Usuario ejecutor = usuarioRepository.findByIdUsuario(idUsuarioLogg)
                .orElseThrow(() -> new BusinessException(
                        UsuarioError.AUTH_USER_NOT_FOUND
                ));

        RoleOpcion permisoRol = roleOpcionRepository
                .findByIdRoleAndIdOpcion(
                        ejecutor.getIdRole(),
                        ControlIdOpcion.USUARIOS.getId()
                )
                .orElse(new RoleOpcion());

        if (permisoRol.getConsultar() == 0) {
            throw new BusinessException(
                    UsuarioError.AUTH_NO_AUTHORIZED_VIEW
            );
        }

        List<Usuario> listado = usuarioRepository.findAll();

        return listado.stream()
                .filter(usuario -> !usuario.getIdUsuario().equals(idUsuarioLogg))
                .map(usuario -> {
                    UsuarioDTO dto = new UsuarioDTO();

                    Sucursal sucursal = sucursalRepository.findById(usuario.getIdSucursal())
                            .orElseThrow(() -> new BusinessException(SucursalError.SUCURSAL_NOT_FOUND));

                    dto.setIdUsuario(usuario.getIdUsuario());
                    dto.setNombre(usuario.getNombre());
                    dto.setApellido(usuario.getApellido());
                    dto.setFechaNacimiento(usuario.getFechaNacimiento());
                    dto.setIdStatusUsuario(usuario.getIdStatusUsuario());
                    dto.setIdGenero(usuario.getIdGenero());
                    dto.setIntentosDeAcceso(usuario.getIntentosDeAcceso());
                    dto.setSesionActual(usuario.getSesionActual());
                    dto.setCorreoElectronico(usuario.getCorreoElectronico());
                    dto.setRequiereCambiarPassword(usuario.getRequiereCambiarPassword());
                    dto.setFotografia(usuario.getFotografia());
                    dto.setTelefonoMovil(usuario.getTelefonoMovil());
                    dto.setIdSucursal(usuario.getIdSucursal());
                    dto.setIdEmpresa(sucursal.getIdEmpresa());
                    dto.setPregunta(usuario.getPregunta());
                    dto.setRespuesta(usuario.getRespuesta());
                    dto.setIdRole(usuario.getIdRole());

                    dto.setFechaCreacion(usuario.getFechaCreacion());
                    dto.setFechaModificacion(usuario.getFechaModificacion());
                    dto.setUsuarioCreacion(usuario.getUsuarioCreacion());
                    dto.setUsuarioModificacion(usuario.getUsuarioModificacion());

                    return dto;
                })
                .toList();
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

        Sucursal sucursal = sucursalRepository.findById(usuario.getIdSucursal())
                .orElseThrow(() -> new BusinessException(SucursalError.SUCURSAL_NOT_FOUND));
        politicasService.validarPasswordEstricto(sucursal.getIdEmpresa(), password);

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

        Sucursal sucursal = sucursalRepository.findById(usuario.getIdSucursal())
                .orElseThrow(() -> new BusinessException(SucursalError.SUCURSAL_NOT_FOUND));
        politicasService.validarPasswordEstricto(sucursal.getIdEmpresa(), passwordNew);

        usuario.setPassword(passwordEncoder.encode(passwordNew));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());
        usuario.setRequiereCambiarPassword(0);
        usuario.setIntentosDeAcceso(0);

        return usuarioRepository.save(usuario);
    }

    public String getPreguntaUsuario(String idUsuarioLog,String ip, String userAgent) {

        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioLog)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        if (usuario.getIdStatusUsuario() != 1) {
            boolean isInactive = usuario.getIdStatusUsuario() == 3;

            auditoriaService.registrarIntento(idUsuarioLog, isInactive ? 4 : 2, ip, userAgent, null);
            throw new BusinessException(isInactive ? UsuarioError.AUTH_USER_INACTIVE : UsuarioError.AUTH_USER_BLOCKED);
        }

        return usuario.getPregunta();
    }

    public boolean validarRespuestaUsuario(String idUsuarioLog, String respuesta,String ip, String userAgent) {
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioLog)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        if (usuario.getIdStatusUsuario() != 1) {
            boolean isInactive = usuario.getIdStatusUsuario() == 3;

            auditoriaService.registrarIntento(idUsuarioLog, isInactive ? 4 : 2, ip, userAgent, null);
            throw new BusinessException(isInactive ? UsuarioError.AUTH_USER_INACTIVE : UsuarioError.AUTH_USER_BLOCKED);
        }

        if (!usuario.getRespuesta().equals(respuesta)) {

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
                throw new BusinessException(UsuarioError.AUTH_INVALID_ANSWER, intentosActuales, intentosPermitidos);
            }
        }

        usuario.setIntentosDeAcceso(0);
        usuarioRepository.save(usuario);

        return true;
    }

    public Usuario cambioPasswordRecuperacion(String idUsuarioLog,String password) {
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuarioLog)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        if (password == null || password.isEmpty()) {
            throw new BusinessException(UsuarioError.AUTH_PASSWORD_EMPTY);
        }

        Sucursal sucursal = sucursalRepository.findById(usuario.getIdSucursal())
                .orElseThrow(() -> new BusinessException(SucursalError.SUCURSAL_NOT_FOUND));

        politicasService.validarPasswordEstricto(sucursal.getIdEmpresa(), password);

        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());
        usuario.setRequiereCambiarPassword(0);
        usuario.setFechaModificacion(LocalDateTime.now());
        usuario.setUsuarioModificacion(usuario.getIdUsuario());

        return usuarioRepository.save(usuario);
    }


    public Usuario consoleUpdateAndSaveUsuario(UsuarioSaveRequest request) {
        String idUsuarioLogg = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario ejecutor = usuarioRepository.findByIdUsuario(idUsuarioLogg)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        RoleOpcion permisoRol = roleOpcionRepository.findByIdRoleAndIdOpcion(ejecutor.getIdRole(), ControlIdOpcion.USUARIOS.getId())
                .orElse(new RoleOpcion());

        Optional<Usuario> optUsuario = usuarioRepository.findByIdUsuario(request.getIdUsuario());

        Usuario data;
        if (optUsuario.isPresent()) {
            if (permisoRol.getCambio() == 0) {
                throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_MODIFY);
            }

            if (!request.getIsUpdate()){
                throw new BusinessException(UsuarioError.AUTH_USER_EXIST);
            }

            data = optUsuario.get();
            data.setNombre(request.getNombre());
            data.setApellido(request.getApellido());
            data.setFechaNacimiento(request.getFechaNacimiento());
            data.setCorreoElectronico(request.getCorreoElectronico());
            data.setTelefonoMovil(request.getTelefonoMovil());
            data.setPregunta(request.getPregunta());
            data.setRespuesta(request.getRespuesta());
            data.setRequiereCambiarPassword(request.getRequiereCambiarPassword());

            data.setIdSucursal(request.getIdSucursal());
            data.setIdGenero(request.getIdGenero());
            data.setIdStatusUsuario(request.getIdStatusUsuario());
            data.setIdRole(request.getIdRole());

            data.setUsuarioModificacion(idUsuarioLogg);
            data.setFechaModificacion(LocalDateTime.now());

            return usuarioRepository.save(data);
        } else {
            if (permisoRol.getAlta() == 0) {
                throw new BusinessException(UsuarioError.AUTH_NO_AUTHORIZED_ADD);
            }
            if (request.getPassword().isEmpty()){
                throw new BusinessException(UsuarioError.AUTH_PASSWORD_EMPTY);
            }

            Sucursal sucursal = sucursalRepository.findById(request.getIdSucursal())
                    .orElseThrow(() -> new BusinessException(SucursalError.SUCURSAL_NOT_FOUND));

            politicasService.validarPasswordEstricto(sucursal.getIdEmpresa(), request.getPassword());

            data = new Usuario();
            data.setIdUsuario(request.getIdUsuario());
            data.setNombre(request.getNombre());
            data.setApellido(request.getApellido());
            data.setFechaNacimiento(request.getFechaNacimiento());
            data.setCorreoElectronico(request.getCorreoElectronico());
            data.setTelefonoMovil(request.getTelefonoMovil());
            data.setPassword(passwordEncoder.encode(request.getPassword()));
            data.setPregunta(request.getPregunta());
            data.setRespuesta(request.getRespuesta());
            data.setRequiereCambiarPassword(1);
            data.setIntentosDeAcceso(0);

            data.setIdSucursal(request.getIdSucursal());
            data.setIdGenero(request.getIdGenero());
            data.setIdStatusUsuario(request.getIdStatusUsuario());
            data.setIdRole(request.getIdRole());

            data.setUsuarioCreacion(idUsuarioLogg);
            data.setFechaCreacion(LocalDateTime.now());

            return usuarioRepository.save(data);
        }
    }
}