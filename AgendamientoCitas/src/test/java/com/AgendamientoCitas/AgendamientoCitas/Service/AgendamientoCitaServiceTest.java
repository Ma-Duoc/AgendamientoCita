package com.AgendamientoCitas.AgendamientoCitas.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.AgendamientoCitas.AgendamientoCitas.Model.*;
import com.AgendamientoCitas.AgendamientoCitas.Repository.AgendamientoCitaRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AgendamientoCitaServiceTest {

    @Autowired
    private AgendamientoCitaService agendamientoCitaService;

    @MockBean
    private AgendamientoCitaRepository agendamientoCitaRepository;

    private final HorarioMedico horario = new HorarioMedico(1, "98765432-1", "10:00", true); // Adaptado

    private final AgendamientoCita cita = new AgendamientoCita(
        1,
        "12345678-9", // rutPaciente
        "98765432-1", // rutMedico
        horario
    );

    @Test
    public void testListarAgendamientos() {
        when(agendamientoCitaRepository.findAll()).thenReturn(List.of(cita));

        List<AgendamientoCita> resultado = agendamientoCitaService.listarAgendamientos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(agendamientoCitaRepository).findAll();
    }

    @Test
    public void testGuardarAgendamiento() {
        when(agendamientoCitaRepository.save(cita)).thenReturn(cita);

        String mensaje = agendamientoCitaService.guardarAgendamiento(cita);

        assertEquals("Agendamiento con ID 1 guardado con éxito", mensaje);
        verify(agendamientoCitaRepository).save(cita);
    }

    @Test
    public void testGuardarAgendamientos() {
        List<AgendamientoCita> lista = List.of(cita);
        when(agendamientoCitaRepository.saveAll(lista)).thenReturn(lista);

        String mensaje = agendamientoCitaService.guardarAgendamientos(lista);

        assertEquals("1 Agendamientos guardados con éxito", mensaje);
        verify(agendamientoCitaRepository).saveAll(lista);
    }

    @Test
    public void testActualizarAgendamiento() {
        when(agendamientoCitaRepository.save(cita)).thenReturn(cita);

        String mensaje = agendamientoCitaService.actualizarAgendamiento(cita);

        assertEquals("Agendamiento con ID 1 actualizado con éxito", mensaje);
        verify(agendamientoCitaRepository).save(cita);
    }

    @Test
    public void testEliminarAgendamiento() {
        doNothing().when(agendamientoCitaRepository).deleteById(1);

        String mensaje = agendamientoCitaService.eliminarAgendamiento(1);

        assertEquals("Agendamiento con ID 1 eliminado con éxito", mensaje);
        verify(agendamientoCitaRepository).deleteById(1);
    }

    @Test
    public void testBuscarPorId() {
        when(agendamientoCitaRepository.findById(1)).thenReturn(Optional.of(cita));

        AgendamientoCita resultado = agendamientoCitaService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCita());
        verify(agendamientoCitaRepository).findById(1);
    }

    @Test
    public void testBuscarPorCliente() {
        when(agendamientoCitaRepository.findByRutPaciente("12345678-9")).thenReturn(List.of(cita));

        List<AgendamientoCita> resultado = agendamientoCitaService.buscarPorPaciente("12345678-9");

        assertEquals(1, resultado.size());
        assertEquals("12345678-9", resultado.get(0).getRutPaciente());
        verify(agendamientoCitaRepository).findByRutPaciente("12345678-9");
    }

    @Test
    public void testBuscarPorMedico() {
        when(agendamientoCitaRepository.findByRutMedico("98765432-1")).thenReturn(List.of(cita));

        List<AgendamientoCita> resultado = agendamientoCitaService.buscarPorMedico("98765432-1");

        assertEquals(1, resultado.size());
        assertEquals("98765432-1", resultado.get(0).getRutMedico());
        verify(agendamientoCitaRepository).findByRutMedico("98765432-1");
    }

    @Test
    public void testBuscarPorHorario() {
        when(agendamientoCitaRepository.findByHorarioIdHorario(1)).thenReturn(Optional.of(cita));

        AgendamientoCita resultado = agendamientoCitaService.buscarPorHorario(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getHorario().getIdHorario());
        verify(agendamientoCitaRepository).findByHorarioIdHorario(1);
    }
}
