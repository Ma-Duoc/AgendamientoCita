package com.AgendamientoCitas.AgendamientoCitas.Controller;

import com.AgendamientoCitas.AgendamientoCitas.Model.*;
import com.AgendamientoCitas.AgendamientoCitas.Service.AgendamientoCitaService;
import com.AgendamientoCitas.AgendamientoCitas.Service.UsuarioService;
import com.AgendamientoCitas.AgendamientoCitas.dto.MedicoDTO;
import com.AgendamientoCitas.AgendamientoCitas.dto.PacienteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendamientoCitaController.class)
public class AgendamientoCitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendamientoCitaService agendamientoCitaService;

    @MockBean
    private UsuarioService usuarioService; // AGREGADO

    @Autowired
    private ObjectMapper objectMapper;

    private HorarioMedico horario;
    private AgendamientoCita cita;

    @BeforeEach
    public void setup() {
        horario = new HorarioMedico(1, "98765432-1", "10:00", true);
        cita = new AgendamientoCita(1, "12345678-9", "98765432-1", horario);
    }

    @Test
    public void testGuardarAgendamiento() throws Exception {
        Mockito.when(usuarioService.obtenerPacientePorRut("12345678-9")).thenReturn(new PacienteDTO());
        Mockito.when(usuarioService.obtenerMedicoPorRut("98765432-1")).thenReturn(new MedicoDTO());
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
        Mockito.when(usuarioService.obtenerPacientePorRut("12345678-9")).thenReturn(new PacienteDTO());
        Mockito.when(usuarioService.obtenerMedicoPorRut("98765432-1")).thenReturn(new MedicoDTO());
        Mockito.when(agendamientoCitaService.buscarPorHorario(anyInt())).thenReturn(cita);

        mockMvc.perform(post("/agendamientos/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("El horario ya está ocupado")));
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
                .andExpect(jsonPath("$[0].rutPaciente").value("12345678-9"));
    }

    @Test
    public void testBuscarPorMedico() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorMedico("98765432-1")).thenReturn(List.of(cita));

        mockMvc.perform(get("/agendamientos/buscar/medico/98765432-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rutMedico").value("98765432-1"));
    }

    @Test
    public void testBuscarPorHorario() throws Exception {
        Mockito.when(agendamientoCitaService.buscarPorHorario(1)).thenReturn(cita);

        mockMvc.perform(get("/agendamientos/buscar/horario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.horario.idHorario").value(1));
    }
}
