package com.AgendamientoCitas.AgendamientoCitas.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rol") // Si la tabla se llama "roles"
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rol_id;

    @Column(nullable = false)
    private String nombre;
}

