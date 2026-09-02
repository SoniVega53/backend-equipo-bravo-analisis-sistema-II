package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.BitacoraAcceso;
import backend_equipo_bravo.analisis_sistema_II.repository.BitacoraAccesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import java.time.LocalDateTime;

@Service
public class AuditoriaService {

    @Autowired
    private BitacoraAccesoRepository bitacoraRepository;

    private final Parser uaParser;

    public AuditoriaService() {
        this.uaParser = new Parser();
    }

    public void registrarIntento(String idUsuario, Integer idTipoAcceso, String ip, String userAgent, String sesionActual) {
        BitacoraAcceso log = new BitacoraAcceso();
        log.setIdUsuario(idUsuario);
        log.setIdTipoAcceso(idTipoAcceso);
        log.setFechaAcceso(LocalDateTime.now());
        log.setDireccionIp(ip);
        log.setHttpUserAgent(userAgent);
        log.setSesion(sesionActual);
        log.setAcceso(idTipoAcceso == 1 ? "Éxito" : "Denegado");

        if (userAgent != null && !userAgent.isEmpty()) {
            Client client = uaParser.parse(userAgent);
            log.setBrowser(client.userAgent.family);
            log.setSistemaOperativo(client.os.family);
            log.setDispositivo(client.device.family);
        }

        bitacoraRepository.save(log);
    }
}