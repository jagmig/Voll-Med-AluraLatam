package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.validaciones.cancelamiento.ValidadorCancelarConsultas;
import med.voll.api.domain.consulta.validaciones.reserva.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.paciente.PacienteRepository;
import med.voll.api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaDeConsultas {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private List<ValidadorDeConsultas> validadores;

    @Autowired
    private List<ValidadorCancelarConsultas> validadorCancelarConsultas;

    public DatosDellateConsulta reservar(DatosReservaConsulta datosReservaConsulta) {
        // Implementar la reserva de la consulta

        if(!pacienteRepository.existsById(datosReservaConsulta.idPaciente())){

            throw new ValidacionException("No existe el paciente con ese id");
        }
        if(datosReservaConsulta.idMedico() != null && !medicoRepository.existsById(datosReservaConsulta.idMedico())){

            throw new ValidacionException("No existe el médico con ese id");
        }

        //validaciones

        validadores.forEach(v -> v.validar(datosReservaConsulta));

        var medico = elegirMedico(datosReservaConsulta);
        if(medico == null){

            throw new ValidacionException("No existe un médico disponible en ese horario");
        }
        var paciente = pacienteRepository.findById(datosReservaConsulta.idPaciente()).get();
        var consulta = new Consulta(null, medico, paciente, datosReservaConsulta.fecha(), null);

        consultaRepository.save(consulta);
        return new DatosDellateConsulta(consulta);

    }

    private Medico elegirMedico(DatosReservaConsulta datosReservaConsulta) {

        if(datosReservaConsulta.idMedico() != null){

            return medicoRepository.getReferenceById(datosReservaConsulta.idMedico());
        }
        if(datosReservaConsulta.especialidad()==null){
            throw new ValidacionException("Debe especificar la especialidad del médico");
        }
        return medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(datosReservaConsulta.especialidad(), datosReservaConsulta.fecha());
    }

    public void cancelarConsulta(DatosCancelamientoConsulta datos) {

        if(!consultaRepository.existsById(datos.idConsulta())){
            throw new ValidacionException("No existe la consulta con ese id");

        }
        //validaciones
        validadorCancelarConsultas.forEach(v -> v.validar(datos));
        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelar(datos.motivo());
    }
}
