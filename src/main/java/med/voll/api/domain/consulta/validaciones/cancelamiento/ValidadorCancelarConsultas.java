package med.voll.api.domain.consulta.validaciones.cancelamiento;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;

@Component
public class ValidadorCancelarConsultas implements ValidadorCancelamientoDeConsulta {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public void validar(DatosCancelamientoConsulta datos) {

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        var horaLocal = LocalTime.now();
        var diferenciaEnHoras = Duration.between(horaLocal, consulta.getFecha().toLocalDate()).toHours();


        if(diferenciaEnHoras < 24){
            throw new ValidacionException("Se deben cancelar las consultas con 24 horas de anticipaci{on");
        }
    }
}
