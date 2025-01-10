package med.voll.api.domain.medico;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.paciente.DatosRegistroPaciente;
import med.voll.api.paciente.Paciente;
import med.voll.api.repository.MedicoRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Deberia devolver null cuando el medico buscado existe pero no está disponible en esa fecha")
    void elegirMedicoAleatorioDisponibleEnLaFechaEscenario1() {
        //given o arrange
        var lunesSiguienteALas10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var medico = registrarMedico("Medico1", "medico@gmail.com", "12345", Especialidad.CARDIOLOGIA);
        var paciente = registrarPaciente("Paciente 1", "paciente@gmail.com", "123555");
        registrarConsulta(medico, paciente, lunesSiguienteALas10);
        //when o act
        var medicoLibre = medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(Especialidad.CARDIOLOGIA, lunesSiguienteALas10);
        //then o assert
        assertThat(medicoLibre).isNull();
    }

    @Test
    @DisplayName("Deberia devolver un medico cuando el medico buscado esta disponible en esa fecha")
    void elegirMedicoAleatorioDisponibleEnLaFechaEscenario2() {
        //given o arrange
        var lunesSiguienteALas10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var medico = registrarMedico("Medico1", "medico@gmail.com", "12345", Especialidad.CARDIOLOGIA);
        //when o act
        var medicoLibre = medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(Especialidad.CARDIOLOGIA, lunesSiguienteALas10);
        //then o assert
        assertThat(medicoLibre).isEqualTo(medico);

    }

    private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha){
        entityManager.persist(new Consulta(null, medico, paciente, fecha, null));
    }

    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad){
        var medico = new Medico(datosMedico(nombre, email, documento, especialidad));
        entityManager.persist(medico);
        return medico;
    }

    private Paciente registrarPaciente(String nombre, String email, String documento){

        var paciente = new Paciente(datosPaciente(nombre, email, documento));
        entityManager.persist(paciente);
        return paciente;
    }

    private DatosRegistroMedico datosMedico(String nombre, String email, String documento, Especialidad especialidad){
        return new DatosRegistroMedico(
                nombre,
                email,
                "333-444-555-5555",
                documento,
                especialidad,
                datosDireccion());
    }

    private DatosRegistroPaciente datosPaciente(String nombre, String email, String documento) {

        return new DatosRegistroPaciente(
                nombre,
                email,
                "922-123-4452",
                documento,
                datosDireccion());
    }

    private DatosDireccion datosDireccion() {
        return new DatosDireccion("Calle 123",
                "7",
                "Ciudad XYZ",
                "55555",
            "1");
    }
}