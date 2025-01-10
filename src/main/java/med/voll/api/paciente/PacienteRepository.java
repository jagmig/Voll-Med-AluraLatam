package med.voll.api.paciente;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {


    Page<Paciente> findByActivoTrue(Pageable paginacion);

    @Query(value = """
            SELECT p.activo
            FROM Paciente p
            WHERE p.id = :idPaciente            
            """, nativeQuery = true)
    boolean findActivoById(Long idPaciente);
}
