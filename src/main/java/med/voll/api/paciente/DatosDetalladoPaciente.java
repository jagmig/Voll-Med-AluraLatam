package med.voll.api.paciente;

public record DatosDetalladoPaciente(
                                     String nombre,
                                     String email,
                                     String documentoIdentidad) {

    public DatosDetalladoPaciente(Paciente paciente) {
        this(paciente.getNombre(), paciente.getEmail(), paciente.getDocumentoIdentidad());
    }
}
