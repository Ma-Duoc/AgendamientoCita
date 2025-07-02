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

    // Obtener paciente por RUT
    public PacienteDTO obtenerPacientePorRut(String rut) {
        return usuarioClient.buscarPacientePorRut(rut);
    }
    
    // Obtener médico por RUT
    public MedicoDTO obtenerMedicoPorRut(String rut) {
        return usuarioClient.buscarMedicoPorRut(rut);
    }
}
