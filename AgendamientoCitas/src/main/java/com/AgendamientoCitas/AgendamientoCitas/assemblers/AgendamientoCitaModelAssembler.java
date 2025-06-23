package com.AgendamientoCitas.AgendamientoCitas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.AgendamientoCitas.AgendamientoCitas.Controller.AgendamientoCitaController;
import com.AgendamientoCitas.AgendamientoCitas.Model.AgendamientoCita;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class AgendamientoCitaModelAssembler implements RepresentationModelAssembler<AgendamientoCita, EntityModel<AgendamientoCita>> {

    @Override
    public EntityModel<AgendamientoCita> toModel(AgendamientoCita agendamiento) {
        return EntityModel.of(agendamiento,
            linkTo(methodOn(AgendamientoCitaController.class).buscarPorId(agendamiento.getIdCita())).withSelfRel(),
            linkTo(methodOn(AgendamientoCitaController.class).listarAgendamientos()).withRel("todos"),
            linkTo(methodOn(AgendamientoCitaController.class).buscarPorPaciente(agendamiento.getPaciente().getRut())).withRel("buscarPorPaciente"),
            linkTo(methodOn(AgendamientoCitaController.class).buscarPorMedico(agendamiento.getMedico().getRut())).withRel("buscarPorMedico"),
            linkTo(methodOn(AgendamientoCitaController.class).buscarPorHorario(agendamiento.getHorario().getIdHorario())).withRel("buscarPorHorario")
        );
    }
}
