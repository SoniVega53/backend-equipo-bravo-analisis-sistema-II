package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.exception.successCode.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

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
}