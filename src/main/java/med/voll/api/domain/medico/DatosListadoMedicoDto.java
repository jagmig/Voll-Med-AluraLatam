package med.voll.api.domain.medico;

public record DatosListadoMedicoDto(
        Long id,
        String nombre,
        Especialidad especialidad,
        String documento,
        String email) {

    public DatosListadoMedicoDto(Medico medico){
        this(medico.getId(), medico.getNombre(), medico.getEspecialidad(), medico.getDocumento(), medico.getEmail());
    }
}
