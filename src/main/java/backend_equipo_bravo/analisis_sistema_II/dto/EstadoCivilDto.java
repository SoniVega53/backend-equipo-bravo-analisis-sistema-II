package backend_equipo_bravo.analisis_sistema_II.dto;

public class EstadoCivilDto {
    private Integer idEstadoCivil;
    private String nombre;
    private Boolean estado;

    public EstadoCivilDto() {}

    public EstadoCivilDto(Integer idEstadoCivil, String nombre, Boolean estado) {
        this.idEstadoCivil = idEstadoCivil;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Integer getIdEstadoCivil() {
        return idEstadoCivil;
    }

    public void setIdEstadoCivil(Integer idEstadoCivil) {
        this.idEstadoCivil = idEstadoCivil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}