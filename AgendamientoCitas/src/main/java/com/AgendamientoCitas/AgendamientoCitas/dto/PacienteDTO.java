package com.AgendamientoCitas.AgendamientoCitas.dto;

import lombok.Data;

@Data
public class PacienteDTO {
    private String rutPaciente;
    private String nombre;
    private String correo;
    private String fono;
    private String historialMedico;
}