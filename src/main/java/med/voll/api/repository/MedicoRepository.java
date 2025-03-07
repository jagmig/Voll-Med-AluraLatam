package med.voll.api.repository;

import med.voll.api.domain.medico.Especialidad;
import med.voll.api.domain.medico.Medico;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;


public interface MedicoRepository extends JpaRepository<Medico, Long> {


    Page<Medico> findByActivoTrue(Pageable paginacion);

    @Query("""
            SELECT m
            FROM Medico m
            WHERE m.activo = true
            AND m.especialidad = :especialidad
            AND m.id not in(
                SELECT c.medico.id FROM Consulta c
                WHERE c.fecha = :fecha
                AND c.motivoCancelamiento is null 
            )
            ORDER BY RANDOM()
            LIMIT 1
         """)
    Medico elegirMedicoAleatorioDisponibleEnLaFecha(Especialidad especialidad, LocalDateTime fecha);


    @Query(value = """
            SELECT m.activo
            FROM Medico m
            WHERE m.id = :idMedico
            """, nativeQuery = true)
    boolean findActivoById(Long idMedico);
}
