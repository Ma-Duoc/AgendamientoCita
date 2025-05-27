package com.AgendamientoCitas.AgendamientoCitas.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medico")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Medico {

    @Id
    @Column(unique = true, length = 13)
    private String rutMedico;

    @Column(nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false, length = 9)
    private String fono;

    @Column(nullable = false, length = 30)
    private String especialidad;
}

