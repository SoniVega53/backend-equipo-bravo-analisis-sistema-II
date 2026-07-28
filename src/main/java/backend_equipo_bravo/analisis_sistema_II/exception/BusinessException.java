package backend_equipo_bravo.analisis_sistema_II.exception;

import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.EmpresaError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.SucursalError;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.UsuarioError;

public class BusinessException extends RuntimeException {
    private final int codigoNumerico;
    private final String codigoTexto;

    // Constructor normal para errores estáticos
    public BusinessException(UsuarioError error) {
        super(error.getMensajeFormato());
        this.codigoNumerico = error.getCodigoNumerico();
        this.codigoTexto = error.getCodigoTexto();
    }

    public BusinessException(UsuarioError error, Object... args) {
        super(String.format(error.getMensajeFormato(), args));
        this.codigoNumerico = error.getCodigoNumerico();
        this.codigoTexto = error.getCodigoTexto();
    }

    // Constructor para errores de Empresa
    public BusinessException(EmpresaError error) {
        super(error.getMensaje());
        this.codigoNumerico = error.getCodigoNumerico();
        this.codigoTexto = error.getCodigoTexto();
    }

    // Constructor para errores de Sucursal
    public BusinessException(SucursalError error) {
        super(error.getMensaje());
        this.codigoNumerico = error.getCodigoNumerico();
        this.codigoTexto = error.getCodigoTexto();
    }

    public int getCodigoNumerico() {
        return codigoNumerico;
    }

    public String getCodigoTexto() {
        return codigoTexto;
    }
}