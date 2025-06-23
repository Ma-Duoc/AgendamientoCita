package com.AgendamientoCitas.AgendamientoCitas.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paciente")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Paciente {

    @Id
    @Column(unique = true, length = 13)
    private String rut;  

    @Column(nullable = false, length = 50,unique = false)
    private String nombre;  

    @Column(nullable = false)
    private String correo;  

    @Column(nullable = false, length = 9)
    private String fono;  

    @Column(nullable = false, columnDefinition = "TEXT")  
    private String historialMedico;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol_id; 

}


