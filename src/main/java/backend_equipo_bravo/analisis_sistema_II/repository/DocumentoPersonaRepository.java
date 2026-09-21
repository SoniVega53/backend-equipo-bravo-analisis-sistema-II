package backend_equipo_bravo.analisis_sistema_II.repository;

import backend_equipo_bravo.analisis_sistema_II.entity.DocumentoPersona;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.DocumentoPersonaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoPersonaRepository extends JpaRepository<DocumentoPersona, DocumentoPersonaId> {
}