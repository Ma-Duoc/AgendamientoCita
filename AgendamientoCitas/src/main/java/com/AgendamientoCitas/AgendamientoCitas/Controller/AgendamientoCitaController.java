package com.AgendamientoCitas.AgendamientoCitas.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.AgendamientoCitas.AgendamientoCitas.Model.AgendamientoCita;
import com.AgendamientoCitas.AgendamientoCitas.Service.AgendamientoCitaService;

@RestController
@RequestMapping("/agendamientos")
public class AgendamientoCitaController {

    @Autowired
    private AgendamientoCitaService agendamientoCitaService;

    @GetMapping("/listar")
    public ResponseEntity<List<AgendamientoCita>> listarAgendamientos() {
        List<AgendamientoCita> agendamientos = agendamientoCitaService.listarAgendamientos();
        if (agendamientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(agendamientos);
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarAgendamiento(@RequestBody AgendamientoCita agendamiento) {
        if (agendamiento.getCliente() == null ||
            agendamiento.getMedico() == null ||
            agendamiento.getHorario() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos cliente, médico y horario son obligatorios.");
        }
        
        AgendamientoCita citaExistente = agendamientoCitaService.buscarPorHorario(agendamiento.getHorario().getIdHorario());
        if (citaExistente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El horario ya está ocupado por otro agendamiento.");
        }

        String mensaje = agendamientoCitaService.guardarAgendamiento(agendamiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PostMapping("/guardar-multiple")
    public ResponseEntity<String> guardarAgendamientos(@RequestBody List<AgendamientoCita> agendamientos) {
        if (agendamientos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista de agendamientos está vacía.");
        }

        for (AgendamientoCita ag : agendamientos) {
            if (ag.getCliente() == null || ag.getMedico() == null || ag.getHorario() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos incompletos en algún agendamiento.");
            }

            if (agendamientoCitaService.buscarPorHorario(ag.getHorario().getIdHorario()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El horario " + ag.getHorario().getIdHorario() + " ya está ocupado.");
            }
        }

        String mensaje = agendamientoCitaService.guardarAgendamientos(agendamientos);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizarAgendamiento(@PathVariable Integer id, @RequestBody AgendamientoCita agendamientoCita) {
        
        AgendamientoCita existente = agendamientoCitaService.buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamiento no encontrado.");
        }

        if (!existente.getMedico().getRutMedico().equals(agendamientoCita.getMedico().getRutMedico())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede cambiar el médico de la cita.");
        }

        AgendamientoCita citaConEseHorario = agendamientoCitaService.buscarPorHorario(agendamientoCita.getHorario().getIdHorario());
        if (citaConEseHorario != null 
            && !citaConEseHorario.getIdCita().equals(id)) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El horario ya está ocupado por otro agendamiento del mismo médico.");
        }

        existente.setHorario(agendamientoCita.getHorario());

        String mensaje = agendamientoCitaService.actualizarAgendamiento(existente);
        return ResponseEntity.ok(mensaje);
    }



    @DeleteMapping("/eliminar/{idCita}")
    public ResponseEntity<String> eliminarAgendamiento(@PathVariable Integer idCita) {
        AgendamientoCita existente = agendamientoCitaService.buscarPorId(idCita);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamiento con ID " + idCita + " no encontrado.");
        }

        String mensaje = agendamientoCitaService.eliminarAgendamiento(idCita);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/buscar/id/{idCita}")
    public ResponseEntity<AgendamientoCita> buscarPorId(@PathVariable Integer idCita) {
        AgendamientoCita agendamiento = agendamientoCitaService.buscarPorId(idCita);
        if (agendamiento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(agendamiento);
    }

    @GetMapping("/buscar/cliente/{rutCliente}")
    public ResponseEntity<List<AgendamientoCita>> buscarPorCliente(@PathVariable String rutCliente) {
        List<AgendamientoCita> lista = agendamientoCitaService.buscarPorCliente(rutCliente);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/medico/{rutMedico}")
    public ResponseEntity<List<AgendamientoCita>> buscarPorMedico(@PathVariable String rutMedico) {
        List<AgendamientoCita> lista = agendamientoCitaService.buscarPorMedico(rutMedico);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/horario/{idHorario}")
    public ResponseEntity<AgendamientoCita> buscarPorHorario(@PathVariable Integer idHorario) {
        AgendamientoCita agendamiento = agendamientoCitaService.buscarPorHorario(idHorario);
        if (agendamiento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(agendamiento);
    }   



}
