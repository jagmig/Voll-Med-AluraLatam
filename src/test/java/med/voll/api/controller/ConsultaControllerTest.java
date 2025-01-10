package med.voll.api.controller;

import med.voll.api.domain.consulta.DatosDellateConsulta;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import med.voll.api.domain.consulta.ReservaDeConsultas;
import med.voll.api.domain.medico.Especialidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<DatosReservaConsulta> datosReservaConsultaJson;

    @Autowired
    private JacksonTester<DatosDellateConsulta> datosDellateConsultaJson;

    @MockitoBean
    private ReservaDeConsultas reservaDeConsultas;

    @Test
    @DisplayName("Deberia devolver el Http 400 cuando la request no tenga datos")
    @WithMockUser
    void reservar_escenario1() throws Exception {

        var response = mockMvc.perform(post("/consultas")).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Deberia devolver el Http 200 cuando la request reciba un json valido")
    @WithMockUser
    void reservar_escenario2() throws Exception {

        var fecha = LocalDateTime.now().plusHours(1);
        var especialidad = Especialidad.CARDIOLOGIA;
        var datosDetalle = new DatosDellateConsulta(null, 2L, 5L, fecha);
        when(reservaDeConsultas.reservar(any())).thenReturn(datosDetalle);

        var response = mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(datosReservaConsultaJson.write(
                                new DatosReservaConsulta(2L, 5L, fecha, especialidad)
                        ).getJson())
                )
                .andReturn().getResponse();
        var jsonEsperado = datosDellateConsultaJson.write
                (datosDetalle).getJson();
        assertEquals(200, response.getStatus());
        assertEquals(jsonEsperado, response.getContentAsString());
    }
}