package med.voll.api.domain.medico;

import med.voll.api.domain.direccion.DatosDireccion;

public record DatosMostrarMedico(

        String nombre,

        String email,

        String telefono,

        String documento,

        Especialidad especialidad,

        DatosDireccion direccion)
 {
}
