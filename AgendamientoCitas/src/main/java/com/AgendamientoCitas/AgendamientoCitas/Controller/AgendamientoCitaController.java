package com.AgendamientoCitas.AgendamientoCitas.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.AgendamientoCitas.AgendamientoCitas.Model.AgendamientoCita;
import com.AgendamientoCitas.AgendamientoCitas.Service.AgendamientoCitaService;
import com.AgendamientoCitas.AgendamientoCitas.Service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/agendamientos")
@Tag(name = "Agendamientos", description = "Operaciones relacionadas con el agendamiento de citas médicas")
public class AgendamientoCitaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AgendamientoCitaService agendamientoCitaService;

    @GetMapping("/listar")
    @Operation(summary = "Listar todos los agendamientos", description = "Retorna todos los agendamientos de citas registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agendamientos encontrados",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AgendamientoCita.class)))),
        @ApiResponse(responseCode = "204", description = "No hay agendamientos registrados", content = @Content)
    })
    public ResponseEntity<List<AgendamientoCita>> listarAgendamientos() {
        List<AgendamientoCita> agendamientos = agendamientoCitaService.listarAgendamientos();
        if (agendamientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(agendamientos);
    }

    @PostMapping("/guardar")
    @Operation(summary = "Guardar un agendamiento", description = "Registra una nueva cita médica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agendamiento creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos faltantes o conflicto de horario")})
    public ResponseEntity<String> guardarAgendamiento( @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos del agendamiento a registrar",
        required = true,
        content = @Content(schema = @Schema(implementation = AgendamientoCita.class)))
    @RequestBody AgendamientoCita agendamiento) {

    if (agendamiento.getRutPaciente() == null || agendamiento.getRutMedico() == null || agendamiento.getHorario() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos paciente, médico y horario son obligatorios.");
    }

    // Validar existencia del paciente (desde microservicio de usuarios)
    try {
        if (usuarioService.obtenerPacientePorRut(agendamiento.getRutPaciente()) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Paciente con RUT " + agendamiento.getRutPaciente() + " no se encuentra registrado.");
        }
    } catch (feign.FeignException.NotFound e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Paciente con RUT " + agendamiento.getRutPaciente() + " no se encuentra registrado.");
    }

    // Validar existencia del médico (desde microservicio de usuarios)
    try {
        if (usuarioService.obtenerMedicoPorRut(agendamiento.getRutMedico()) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(" Médico con RUT " + agendamiento.getRutMedico() + " no se encuentra registrado.");
        }
    } catch (feign.FeignException.NotFound e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Médico con RUT " + agendamiento.getRutMedico() + " no se encuentra registrado.");
    }

    AgendamientoCita citaExistente = agendamientoCitaService.buscarPorHorario(agendamiento.getHorario().getIdHorario());
    if (citaExistente != null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El horario ya está ocupado por otro agendamiento.");
    }

    // Guardar el agendamiento
    String mensaje = agendamientoCitaService.guardarAgendamiento(agendamiento);
    return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
}


    @PostMapping("/guardar-multiple")
    @Operation(summary = "Guardar múltiples agendamientos", description = "Registra una lista de citas médicas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agendamientos creados correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos faltantes o conflictos de horario en uno o más registros")
    })
    public ResponseEntity<String> guardarAgendamientos(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Lista de agendamientos a registrar",
            required = true,
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AgendamientoCita.class)))
        )
        @RequestBody List<AgendamientoCita> agendamientos) {

        if (agendamientos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista de agendamientos está vacía.");
        }

        for (AgendamientoCita ag : agendamientos) {
            // Validación de campos obligatorios
            if (ag.getRutPaciente() == null || ag.getRutMedico() == null || ag.getHorario() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Datos incompletos en algún agendamiento.");
            }

            // Validar si el horario ya está ocupado
            if (agendamientoCitaService.buscarPorHorario(ag.getHorario().getIdHorario()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El horario " + ag.getHorario().getIdHorario() + " ya está ocupado.");
            }

            // Validar existencia del paciente en el microservicio de usuarios
            try {
                if (usuarioService.obtenerPacientePorRut(ag.getRutPaciente()) == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Paciente con RUT " + ag.getRutPaciente() + " no está registrado.");
                }
            } catch (feign.FeignException.NotFound e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Paciente con RUT " + ag.getRutPaciente() + " no está registrado.");
            }

            // Validar existencia del médico en el microservicio de usuarios
            try {
                if (usuarioService.obtenerMedicoPorRut(ag.getRutMedico()) == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Médico con RUT " + ag.getRutMedico() + " no está registrado.");
                }
            } catch (feign.FeignException.NotFound e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Médico con RUT " + ag.getRutMedico() + " no está registrado.");
            }
        }

        // Si pasa todas las validaciones
        String mensaje = agendamientoCitaService.guardarAgendamientos(agendamientos);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }


    @PutMapping("/actualizar/{id}")
    @Operation(summary = "Actualizar agendamiento", description = "Permite modificar el horario de una cita médica, sin cambiar el médico asignado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agendamiento actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "No se puede cambiar el médico o el horario ya está ocupado"),
        @ApiResponse(responseCode = "404", description = "Agendamiento no encontrado")
    })
    public ResponseEntity<String> actualizarAgendamiento(
        @Parameter(description = "ID del agendamiento a actualizar", required = true) @PathVariable Integer id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos actualizados del agendamiento",
            required = true,
            content = @Content(schema = @Schema(implementation = AgendamientoCita.class))
        )
        @RequestBody AgendamientoCita agendamientoCita) {

        AgendamientoCita existente = agendamientoCitaService.buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamiento no encontrado.");
        }

        if (!existente.getRutMedico().equals(agendamientoCita.getRutMedico())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede cambiar el médico de la cita.");
        }

        AgendamientoCita citaConEseHorario = agendamientoCitaService.buscarPorHorario(agendamientoCita.getHorario().getIdHorario());
        if (citaConEseHorario != null && !citaConEseHorario.getIdCita().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El horario ya está ocupado por otro agendamiento del mismo médico.");
        }

        existente.setHorario(agendamientoCita.getHorario());

        String mensaje = agendamientoCitaService.actualizarAgendamiento(existente);
        return ResponseEntity.ok(mensaje);
    }

    @DeleteMapping("/eliminar/{idCita}")
    @Operation(summary = "Eliminar agendamiento", description = "Elimina un agendamiento de cita médica según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agendamiento eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Agendamiento no encontrado")
    })
    public ResponseEntity<String> eliminarAgendamiento(
        @Parameter(description = "ID del agendamiento a eliminar", required = true)
        @PathVariable Integer idCita) {

        AgendamientoCita existente = agendamientoCitaService.buscarPorId(idCita);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamiento con ID " + idCita + " no encontrado.");
        }

        String mensaje = agendamientoCitaService.eliminarAgendamiento(idCita);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/buscar/id/{idCita}")
    @Operation(summary = "Buscar agendamiento por ID", description = "Obtiene un agendamiento de cita por su identificador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agendamiento encontrado",
            content = @Content(schema = @Schema(implementation = AgendamientoCita.class))),
        @ApiResponse(responseCode = "404", description = "Agendamiento no encontrado")
    })
    public ResponseEntity<AgendamientoCita> buscarPorId(
        @Parameter(description = "ID del agendamiento", required = true)
        @PathVariable Integer idCita) {

        AgendamientoCita agendamiento = agendamientoCitaService.buscarPorId(idCita);
        if (agendamiento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(agendamiento);
    }

    @GetMapping("/buscar/paciente/{rutPaciente}")
    @Operation(summary = "Buscar agendamientos por paciente", description = "Obtiene todas las citas médicas asociadas al RUT de un paciente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Citas encontradas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AgendamientoCita.class)))),
        @ApiResponse(responseCode = "204", description = "No se encontraron citas para el paciente")
    })
    public ResponseEntity<List<AgendamientoCita>> buscarPorPaciente(
        @Parameter(description = "RUT del paciente", required = true)
        @PathVariable String rutPaciente) {

        List<AgendamientoCita> lista = agendamientoCitaService.buscarPorPaciente(rutPaciente);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/medico/{rutMedico}")
    @Operation(summary = "Buscar agendamientos por médico", description = "Obtiene todas las citas médicas de un médico por su RUT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Citas encontradas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AgendamientoCita.class)))),
        @ApiResponse(responseCode = "204", description = "No se encontraron citas para el médico")
    })
    public ResponseEntity<List<AgendamientoCita>> buscarPorMedico(
        @Parameter(description = "RUT del médico", required = true)
        @PathVariable String rutMedico) {

        List<AgendamientoCita> lista = agendamientoCitaService.buscarPorMedico(rutMedico);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/horario/{idHorario}")
    @Operation(summary = "Buscar agendamiento por horario", description = "Obtiene el agendamiento de una cita médica según el ID del horario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita encontrada",
            content = @Content(schema = @Schema(implementation = AgendamientoCita.class))),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada para el horario indicado")
    })
    public ResponseEntity<AgendamientoCita> buscarPorHorario(
        @Parameter(description = "ID del horario", required = true)
        @PathVariable Integer idHorario) {

        AgendamientoCita agendamiento = agendamientoCitaService.buscarPorHorario(idHorario);
        if (agendamiento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(agendamiento);
    }
}

