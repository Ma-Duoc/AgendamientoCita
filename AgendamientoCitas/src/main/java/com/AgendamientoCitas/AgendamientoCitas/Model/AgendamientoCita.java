package com.AgendamientoCitas.AgendamientoCitas.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="agendamiento_cita")
public class AgendamientoCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCita;

    @ManyToOne
    @JoinColumn(name = "rut_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "rut_medico", nullable = false)
    private Medico medico;
    
   @OneToOne
   @JoinColumn(name = "id_horario", nullable = false, unique = true)
   private HorarioMedico horario;

}
