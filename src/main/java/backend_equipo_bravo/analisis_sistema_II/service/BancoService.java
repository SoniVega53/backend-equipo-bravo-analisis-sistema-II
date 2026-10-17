
package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.entity.Banco;
import backend_equipo_bravo.analisis_sistema_II.repository.BancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class BancoService extends BaseService<Banco, Integer> {

    @Autowired
    private BancoRepository bancoRepository;

    @Override
    protected JpaRepository<Banco, Integer> getRepository() {
        return bancoRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new RuntimeException(
            "No se encontró el banco con ID: " + id
        );
    }
}
