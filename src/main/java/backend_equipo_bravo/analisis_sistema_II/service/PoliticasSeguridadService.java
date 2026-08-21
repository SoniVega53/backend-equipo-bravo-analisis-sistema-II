package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.PasswordPolicyDto;
import backend_equipo_bravo.analisis_sistema_II.entity.Empresa;
import backend_equipo_bravo.analisis_sistema_II.entity.Sucursal;
import backend_equipo_bravo.analisis_sistema_II.entity.Usuario;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.EmpresaError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.SucursalError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;
import backend_equipo_bravo.analisis_sistema_II.repository.EmpresaRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.SucursalRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PoliticasSeguridadService {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public int obtenerIntentosPermitidosPorSucursal(Integer idSucursal) {
        Sucursal sucursal = sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new BusinessException(SucursalError.SUCURSAL_NOT_FOUND));

        Empresa empresa = empresaRepository.findById(sucursal.getIdEmpresa())
                .orElseThrow(() -> new BusinessException(EmpresaError.EMPRESA_NOT_FOUND));

        return empresa.getPasswordIntentosAntesDeBloquear() != null
                ? empresa.getPasswordIntentosAntesDeBloquear()
                : 3;
    }

    public PasswordPolicyDto obtenerPoliticaPasswordBase(String idUsuarioLog) {
        Integer idSucursal = usuarioRepository
                .findIdSucursalByIdUsuario(idUsuarioLog)
                .orElseThrow(() -> new BusinessException(UsuarioError.AUTH_USER_NOT_FOUND));

        Sucursal sucursal = sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new BusinessException(SucursalError.SUCURSAL_NOT_FOUND));

        Empresa empresa = empresaRepository.findById(sucursal.getIdEmpresa())
                .orElseThrow(() -> new BusinessException(EmpresaError.EMPRESA_NOT_FOUND));

        StringBuilder regex = new StringBuilder("^");
        StringBuilder mensaje = new StringBuilder("La contraseña debe tener");

        int minMayus = nvl(empresa.getPasswordCantidadMayusculas());
        if (minMayus > 0) {
            regex.append("(?=(?:.*?[A-Z]){").append(minMayus).append("})");
            mensaje.append(", ").append(minMayus).append(" mayúscula(s)");
        }

        int minMinus = nvl(empresa.getPasswordCantidadMinusculas());
        if (minMinus > 0) {
            regex.append("(?=(?:.*?[a-z]){").append(minMinus).append("})");
            mensaje.append(", ").append(minMinus).append(" minúscula(s)");
        }

        int minNumeros = nvl(empresa.getPasswordCantidadNumeros());
        if (minNumeros > 0) {
            regex.append("(?=(?:.*?\\d){").append(minNumeros).append("})");
            mensaje.append(", ").append(minNumeros).append(" número(s)");
        }

        int minEspeciales = nvl(empresa.getPasswordCantidadCaracteresEspeciales());
        if (minEspeciales > 0) {
            regex.append("(?=(?:.*?[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>\\/?-]){").append(minEspeciales).append("})");
            mensaje.append(", ").append(minEspeciales).append(" carácter(es) especial(es)");
        }

        int largoMinimo = nvl(empresa.getPasswordLargo(), 8);
        regex.append(".{").append(largoMinimo).append(",}$");

        mensaje.append(" y un mínimo de ").append(largoMinimo).append(" caracteres.");

        String mensajeFinal = mensaje.toString().replace("tener, ", "tener al menos: ");

        return PasswordPolicyDto.builder()
                .regex(regex.toString())
                .mensajeValidacion(mensajeFinal)
                .largoMinimo(largoMinimo)
                .build();
    }


    public PasswordPolicyDto obtenerPoliticaPassword() {
        String idUsuarioLog = SecurityContextHolder.getContext().getAuthentication().getName();

        return obtenerPoliticaPasswordBase(idUsuarioLog);
    }

    private int nvl(Integer valor) {
        return valor != null ? valor : 0;
    }

    private int nvl(Integer valor, int valorPorDefecto) {
        return valor != null ? valor : valorPorDefecto;
    }
}