package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import org.springframework.stereotype.Service;


@Service
public class BoletaPlanillaService extends ReportePlanillaService {


    public BoletaPlanillaService() {
        setEntityName("BoletaPago");
    }

    @Override
    protected RuntimeException getNotFoundException(Object id) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }
}