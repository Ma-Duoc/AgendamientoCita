package com.AgendamientoCitas.AgendamientoCitas.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AgendamientoCitas.AgendamientoCitas.Model.AgendamientoCita;

@Repository
public interface AgendamientoCitaRepository extends JpaRepository<AgendamientoCita, Integer> {

    List<AgendamientoCita> findByRutPaciente(String rutPaciente);

    List<AgendamientoCita> findByRutMedico(String rutMedico);

    Optional<AgendamientoCita> findByHorarioIdHorario(Integer idHorario);

}

