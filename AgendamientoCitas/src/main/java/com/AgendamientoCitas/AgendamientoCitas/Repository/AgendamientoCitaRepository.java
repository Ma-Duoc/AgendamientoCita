package com.AgendamientoCitas.AgendamientoCitas.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AgendamientoCitas.AgendamientoCitas.Model.AgendamientoCita;

@Repository
public interface AgendamientoCitaRepository extends JpaRepository<AgendamientoCita, Integer> {

    List<AgendamientoCita> findByPacienteRut(String rut);

    List<AgendamientoCita> findByMedicoRut(String rut);

    Optional<AgendamientoCita> findByHorarioIdHorario(Integer idHorario);

}

