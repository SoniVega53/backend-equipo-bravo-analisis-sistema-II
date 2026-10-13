package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.EstadoCivil;
import backend_equipo_bravo.analisis_sistema_II.repository.EstadoCivilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoCivilService {

    @Autowired
    private EstadoCivilRepository estadoCivilRepository;

    public List<EstadoCivil> listarTodos() {
        return estadoCivilRepository.findAll();
    }

    public Optional<EstadoCivil> obtenerPorId(Integer id) {
        return estadoCivilRepository.findById(id);
    }

    public EstadoCivil guardar(EstadoCivil estadoCivil) {
        return estadoCivilRepository.save(estadoCivil);
    }

    public void eliminar(Integer id) {
        estadoCivilRepository.deleteById(id);
    }
}