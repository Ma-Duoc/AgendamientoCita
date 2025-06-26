package com.AgendamientoCitas.AgendamientoCitas.clients;

import com.AgendamientoCitas.AgendamientoCitas.dto.PacienteDTO;
import com.AgendamientoCitas.AgendamientoCitas.dto.MedicoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "gestionycreacionuser", url = "http://localhost:8081")
public interface UsuarioClient {

    // Pacientes - sólo lectura
    @GetMapping("/pacientes/listar")
    List<PacienteDTO> listarPacientes();

    @GetMapping("/pacientes/buscar/rut/{rut}")
    PacienteDTO buscarPacientePorRut(@PathVariable("rut") String rut);

    // Médicos - sólo lectura
    @GetMapping("/medicos/listar")
    List<MedicoDTO> listarMedicos();

    @GetMapping("/medicos/buscar/rut/{rut}")
    MedicoDTO buscarMedicoPorRut(@PathVariable("rut") String rut);
}
