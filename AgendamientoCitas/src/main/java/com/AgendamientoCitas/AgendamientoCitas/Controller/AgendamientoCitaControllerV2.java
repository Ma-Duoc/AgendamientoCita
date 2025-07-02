package com.AgendamientoCitas.AgendamientoCitas.Controller;

import com.AgendamientoCitas.AgendamientoCitas.Model.AgendamientoCita;
import com.AgendamientoCitas.AgendamientoCitas.Service.AgendamientoCitaService;
import com.AgendamientoCitas.AgendamientoCitas.assemblers.AgendamientoCitaModelAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/agendamientos")
public class AgendamientoCitaControllerV2 {

    @Autowired
    private AgendamientoCitaService agendamientoCitaService;

    @Autowired
    private AgendamientoCitaModelAssembler assembler;

    @Operation(summary = "Listar todos los agendamientos")
    @ApiResponse(
        responseCode = "200",
        description = "Listado completo de agendamientos",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = CollectionModel.class)
        )
    )
    @GetMapping(value = "/listar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<AgendamientoCita>>> listarAgendamientos() {
        List<AgendamientoCita> agendamientos = agendamientoCitaService.listarAgendamientos();
        List<EntityModel<AgendamientoCita>> agendamientosModel = agendamientos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(agendamientosModel,
                        linkTo(methodOn(AgendamientoCitaControllerV2.class).listarAgendamientos()).withSelfRel()));
    }

    @Operation(summary = "Buscar agendamiento por ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Agendamiento encontrado",
            content = @Content(
                mediaType = "application/hal+json",
                schema = @Schema(implementation = EntityModel.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Agendamiento no encontrado")
    })
    @GetMapping(value = "/buscar/id/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AgendamientoCita>> buscarPorId(@PathVariable Integer id) {
        AgendamientoCita agendamiento = agendamientoCitaService.buscarPorId(id);
        if (agendamiento == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(agendamiento));
    }

    @Operation(summary = "Buscar agendamientos por paciente (RUT)")
    @ApiResponse(
        responseCode = "200",
        description = "Agendamientos encontrados por paciente",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = CollectionModel.class)
        )
    )
    @GetMapping(value = "/buscar/paciente/{rut}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<AgendamientoCita>>> buscarPorPaciente(@PathVariable String rut) {
        List<AgendamientoCita> lista = agendamientoCitaService.buscarPorPaciente(rut);
        List<EntityModel<AgendamientoCita>> listaModel = lista.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(listaModel,
                        linkTo(methodOn(AgendamientoCitaControllerV2.class).buscarPorPaciente(rut)).withSelfRel()));
    }

    @Operation(summary = "Buscar agendamientos por médico (RUT)")
    @ApiResponse(
        responseCode = "200",
        description = "Agendamientos encontrados por médico",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = CollectionModel.class)
        )
    )
    @GetMapping(value = "/buscar/medico/{rut}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<AgendamientoCita>>> buscarPorMedico(@PathVariable String rut) {
        List<AgendamientoCita> lista = agendamientoCitaService.buscarPorMedico(rut);
        List<EntityModel<AgendamientoCita>> listaModel = lista.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(listaModel,
                        linkTo(methodOn(AgendamientoCitaControllerV2.class).buscarPorMedico(rut)).withSelfRel()));
    }

    @Operation(summary = "Buscar agendamiento por horario (ID de horario)")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Agendamiento encontrado por horario",
            content = @Content(
                mediaType = "application/hal+json",
                schema = @Schema(implementation = EntityModel.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Agendamiento no encontrado")
    })
    @GetMapping(value = "/buscar/horario/{idHorario}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AgendamientoCita>> buscarPorHorario(@PathVariable Integer idHorario) {
        AgendamientoCita agendamiento = agendamientoCitaService.buscarPorHorario(idHorario);
        if (agendamiento == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(agendamiento));
    }
}
