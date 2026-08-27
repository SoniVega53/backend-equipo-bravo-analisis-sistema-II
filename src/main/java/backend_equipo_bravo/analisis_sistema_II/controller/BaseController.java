package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class BaseController {
    public static final Logger log = LoggerFactory.getLogger(BaseController.class);

    protected ResponseEntity<Map<String, Object>> success(Object data, SuccessCode successCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("exito", true);
        response.put("codigoNumerico", successCode.getCodigoNumerico());
        response.put("codigoTexto", successCode.getCodigoTexto());
        response.put("mensaje", successCode.getMensaje());
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ResponseEntity<Map<String, Object>> successMessage(String mensaje) {
        Map<String, Object> response = new HashMap<>();
        response.put("exito", true);
        response.put("codigoNumerico", 200);
        response.put("codigoTexto", "SUCCESS");
        response.put("mensaje", mensaje);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ResponseEntity<Map<String, Object>> error(int codigoNumerico, String codigoTexto, String mensaje, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("exito", false);
        response.put("codigoNumerico", codigoNumerico);
        response.put("codigoTexto", codigoTexto);
        response.put("mensaje", mensaje);
        return new ResponseEntity<>(response, status);
    }

    protected boolean validarPasswordConRegex(String password, String regexPattern) {
        if (password == null || regexPattern == null) {
            return false;
        }

        try {
            return Pattern.compile(regexPattern).matcher(password).matches();
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
}