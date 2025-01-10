create table consultas (

    id bigint not null PRIMARY KEY,
    medico_id bigint not null,
    paciente_id bigint not null,
    fecha TIMESTAMP not null,

    constraint fk_consultas_medico_id foreign key(medico_id) references medico(id),
    constraint fk_consultas_paciente_id foreign key(paciente_id) references paciente(id)

);