package com.AgendamientoCitas.AgendamientoCitas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AgendamientoCitas.AgendamientoCitas.Model.AgendamientoCita;
import com.AgendamientoCitas.AgendamientoCitas.Repository.AgendamientoCitaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AgendamientoCitaService {

    @Autowired
    private AgendamientoCitaRepository agendamientoCitaRepository;

    public List<AgendamientoCita> listarAgendamientos() {
        return agendamientoCitaRepository.findAll();
    }

    public String guardarAgendamiento(AgendamientoCita agendamientoCita) {
        agendamientoCitaRepository.save(agendamientoCita);
        return "Agendamiento con ID " + agendamientoCita.getIdCita() + " guardado con éxito";
    }

    public String guardarAgendamientos(List<AgendamientoCita> agendamientos) {
        agendamientoCitaRepository.saveAll(agendamientos);
        return agendamientos.size() + " Agendamientos guardados con éxito";
    }

    public String actualizarAgendamiento(AgendamientoCita agendamientoCita) {
        agendamientoCitaRepository.save(agendamientoCita);
        return "Agendamiento con ID " + agendamientoCita.getIdCita() + " actualizado con éxito";
    }

    public String eliminarAgendamiento(Integer idCita) {
        agendamientoCitaRepository.deleteById(idCita);
        return "Agendamiento con ID " + idCita + " eliminado con éxito";
    }

    public AgendamientoCita buscarPorId(Integer idCita) {
        return agendamientoCitaRepository.findById(idCita).orElse(null);
        
    }

    public List<AgendamientoCita> buscarPorCliente(String rutCliente) {
    return agendamientoCitaRepository.findByClienteRutCliente(rutCliente);
    }

    public List<AgendamientoCita> buscarPorMedico(String rutMedico) {
        return agendamientoCitaRepository.findByMedicoRutMedico(rutMedico);
    }

    public AgendamientoCita buscarPorHorario(Integer idHorario) {
        return agendamientoCitaRepository.findByHorarioIdHorario(idHorario).orElse(null);
    }

}

