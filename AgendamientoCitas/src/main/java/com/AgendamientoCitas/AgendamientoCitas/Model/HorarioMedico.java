package com.AgendamientoCitas.AgendamientoCitas.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "horario_medico")
public class HorarioMedico {

    @Id
    @Column(unique = true)
    private int idHorario;  


    @Column(name = "rut_medico", nullable = false)
    private String rutMedico;


    @Column(nullable = false,length = 5)
    private String hora;

    @Column(nullable = false)
    private boolean disponibilidad;
}

