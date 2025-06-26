package com.AgendamientoCitas.AgendamientoCitas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AgendamientoCitas.AgendamientoCitas.clients.UsuarioClient;
import com.AgendamientoCitas.AgendamientoCitas.dto.MedicoDTO;
import com.AgendamientoCitas.AgendamientoCitas.dto.PacienteDTO;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioClient usuarioClient;

    // Obtener lista completa de pacientes
    public List<PacienteDTO> obtenerTodosLosPacientes() {
        return usuarioClient.listarPacientes();
    }

    // Obtener paciente por RUT
    public PacienteDTO obtenerPacientePorRut(String rut) {
        return usuarioClient.buscarPacientePorRut(rut);
    }

    // Obtener lista completa de médicos
    public List<MedicoDTO> obtenerTodosLosMedicos() {
        return usuarioClient.listarMedicos();
    }

    // Obtener médico por RUT
    public MedicoDTO obtenerMedicoPorRut(String rut) {
        return usuarioClient.buscarMedicoPorRut(rut);
    }
}
