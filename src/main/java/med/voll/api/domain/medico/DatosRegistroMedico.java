package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRegistroMedico(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email es incorrecto")
        String email,

        @NotBlank(message = "Teléfono es obligatorio")
        String telefono,

        @NotBlank@NotBlank(message = "CRM es obligatorio")
        @Pattern(regexp = "\\d{4,6}", message = "Formato del documento es inválido, solo se aceptan de 4 a 6 números")
        String documento,

        @NotNull(message = "Especialidad es obligatorio")
        Especialidad especialidad,

        @NotNull(message = "Datos de dirección son obligatorios")
        @Valid
        DatosDireccion direccion)
{
}
