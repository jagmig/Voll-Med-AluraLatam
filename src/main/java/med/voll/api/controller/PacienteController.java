package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @GetMapping
    public ResponseEntity<Page<DatosListaPaciente>> listarPacientes(Pageable paginacion){

        var page = repository.findByActivoTrue(paginacion).map(DatosListaPaciente::new);
        return ResponseEntity.ok(page);

    }

    @PostMapping
    @Transactional
    public ResponseEntity crearPaciente(@RequestBody @Valid DatosRegistroPaciente datos,
                                        UriComponentsBuilder uriComponentsBuilder){
        var paciente = new Paciente(datos);
        repository.save(paciente);

        var uri = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetalladoPaciente(paciente));
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarPaciente(@RequestBody @Valid DatosActualizacionPaciente datosActualizacionPaciente){

        Paciente paciente = repository.getReferenceById(datosActualizacionPaciente.id());
        paciente.atualizarInformacion(datosActualizacionPaciente);

        return ResponseEntity.ok(new DatosDetalladoPaciente(paciente));
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity eliminarPaciente(@PathVariable Long id){

        var paciente = repository.getReferenceById(id);
       paciente.inactivar();

       return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity detallar(@PathVariable Long id){

        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DatosDetalladoPaciente(paciente));

    }

}
