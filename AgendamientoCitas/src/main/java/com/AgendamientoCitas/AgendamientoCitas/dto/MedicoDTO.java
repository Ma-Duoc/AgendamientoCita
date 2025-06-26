package com.AgendamientoCitas.AgendamientoCitas.dto;

import lombok.Data;

@Data
public class MedicoDTO {
    private String rutMedico;
    private String nombre;
    private String correo;
    private String fono;
    private String especialidad;
}