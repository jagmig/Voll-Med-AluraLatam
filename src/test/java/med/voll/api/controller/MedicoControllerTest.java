package med.voll.api.controller;


import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.direccion.Direccion;
import med.voll.api.domain.medico.*;
import med.voll.api.repository.MedicoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<DatosRegistroMedico> datosRegistroMedicoJson;

    @Autowired
    private JacksonTester<DatosMostrarMedico> datosMostrarMedicoJson;

    @MockitoBean
    private MedicoRepository repository;


    @Test
    @DisplayName("Deberia devolver el Http 400 cuando la request no tenga datos")
    @WithMockUser
    void registrarMedico_escenario1() throws Exception {

        var response = mockMvc.perform(post("/medicos")).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Deberia devolver el Http 200 cuando la request reciba un json valido")
    @WithMockUser
    void reservar_escenario2() throws Exception {

        var datosRegistro = new DatosRegistroMedico(
                "Jorge",
                  "jagmig@gmail.com",
                "922-206-8044",
                "612466",
                Especialidad.CARDIOLOGIA,
                datosDireccion());

        when(repository.save(any())).thenReturn(new Medico(datosRegistro));

        var response = mockMvc.perform(post("/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(datosRegistroMedicoJson.write(datosRegistro).getJson()))
                        .andReturn().getResponse();

        var datosMostar = new DatosMostrarMedico(
                datosRegistro.nombre(),
                datosRegistro.email(),
                datosRegistro.telefono(),
                datosRegistro.documento(),
                datosRegistro.especialidad(),
                datosRegistro.direccion()
        );

        var jsonEsperado = datosMostrarMedicoJson.write(datosMostar).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }
    private DatosDireccion datosDireccion() {
        return new DatosDireccion(
                "calle ejemplo",
                "7",
                "Minatitlan",
                "Minatitlan",
                "1A");
    }
}