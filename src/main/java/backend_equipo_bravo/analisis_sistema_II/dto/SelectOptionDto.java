package backend_equipo_bravo.analisis_sistema_II.dto;

import lombok.Data;

@Data
public class SelectOptionDto {
    private Object codigo;
    private String valor;
    private Integer seleccionado;

    public SelectOptionDto(Object codigo, String valor) {
        this.codigo = codigo;
        this.valor = valor;
        this.seleccionado = 0;
    }
}