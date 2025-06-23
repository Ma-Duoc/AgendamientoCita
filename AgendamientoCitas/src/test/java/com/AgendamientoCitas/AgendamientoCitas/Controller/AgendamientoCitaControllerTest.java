package com.AgendamientoCitas.AgendamientoCitas.Controller;

import com.AgendamientoCitas.AgendamientoCitas.Model.*;
import com.AgendamientoCitas.AgendamientoCitas.Service.AgendamientoCitaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;


@WebMvcTest(AgendamientoCitaController.class)
public class AgendamientoCitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendamientoCitaService agendamientoCitaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Rol rolPaciente;
    private Rol rolMedico;
    private Paciente paciente;
    private Medico medico;
    private HorarioMedico horario;
    private AgendamientoCita cita;

    @BeforeEach
    public void setup() {
        rolPaciente = new Rol(1, "Paciente");
        rolMedico = new Rol(2, "Medico");

        paciente = new Paciente("12345678-9", "Juan Pérez", "juan@example.com", "912345678", "Sin historial", rolPaciente);
        medico = new Medico("98765432-1", "Dra. Soto", "soto@example.com", "987654321", "Dermatología", rolMedico);
        horario = new HorarioMedico(1, medico, "10:00", true);
        cita = new AgendamientoCita(1, paciente, medico, horario);
    }

    @Test
    public void testListarAgendamientos() throws Exception {
        List<AgendamientoCita> lista = Arrays.asList(cita);
        Mockito.when(agendamientoCitaService.listarAgendamientos()).thenReturn(lista);

        mockMvc.perform(get("/agendamientos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCita").value(cita.getIdCita()));
    }

    @Test
    public void testGuardarAgendamiento() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorHorario(anyInt())).thenReturn(null);
        Mockito.when(agendamientoCitaService.guardarAgendamiento(any(AgendamientoCita.class))).thenReturn("Agendamiento creado");

        mockMvc.perform(post("/agendamientos/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Agendamiento creado")));
    }

    @Test
    public void testGuardarAgendamientoHorarioOcupado() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorHorario(anyInt())).thenReturn(cita);

        mockMvc.perform(post("/agendamientos/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("El horario ya está ocupado por otro agendamiento.")));
    }

    @Test
    public void testActualizarAgendamiento() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorId(1)).thenReturn(cita);
        Mockito.when(agendamientoCitaService.buscarPorHorario(anyInt())).thenReturn(null);
        Mockito.when(agendamientoCitaService.actualizarAgendamiento(any(AgendamientoCita.class))).thenReturn("Cita actualizada");

        mockMvc.perform(put("/agendamientos/actualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Cita actualizada")));
    }

    @Test
    public void testEliminarAgendamiento() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorId(1)).thenReturn(cita);
        Mockito.when(agendamientoCitaService.eliminarAgendamiento(1)).thenReturn("Cita eliminada");

        mockMvc.perform(delete("/agendamientos/eliminar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Cita eliminada")));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorId(1)).thenReturn(cita);

        mockMvc.perform(get("/agendamientos/buscar/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCita").value(1));
    }

    @Test
    public void testBuscarPorPaciente() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorPaciente("12345678-9")).thenReturn(List.of(cita));

        mockMvc.perform(get("/agendamientos/buscar/paciente/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paciente.rut").value("12345678-9"));
    }

    @Test
    public void testBuscarPorMedico() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorMedico("98765432-1")).thenReturn(List.of(cita));

        mockMvc.perform(get("/agendamientos/buscar/medico/98765432-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].medico.rut").value("98765432-1"));
    }

    @Test
    public void testBuscarPorHorario() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorHorario(1)).thenReturn(cita);

        mockMvc.perform(get("/agendamientos/buscar/horario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.horario.idHorario").value(1));
    }
}
