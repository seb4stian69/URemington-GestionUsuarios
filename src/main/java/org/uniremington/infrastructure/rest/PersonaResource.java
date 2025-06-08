package org.uniremington.infrastructure.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uniremington.application.dto.PersonaDto;
import org.uniremington.application.service.PersonaService;
import org.uniremington.domain.model.Persona;
import org.uniremington.shared.exception.NotFoundException;
import org.uniremington.shared.util.PersonaMapper;

import java.util.List;
import java.util.stream.Collectors;

@Path("/personas")
public class PersonaResource {

    @Inject /*->*/ PersonaService service;
    @Inject /*->*/ PersonaMapper mapper;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {

        List<PersonaDto> result = service.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        String json;

        try {
            json = mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Response.ok(json).build();

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {

        PersonaDto persona = service.findById(id)
            .map(mapper::toDto)
            .orElseThrow(
                () -> new NotFoundException("Persona con ID " + id + " no encontrada", "PersonaResource.class")
            );

        ObjectMapper mapper = new ObjectMapper();
        String json;

        try {
            json = mapper.writeValueAsString(persona);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Response.ok(json).build();

    }

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PersonaDto dto) {

        Persona model = mapper.toModel(dto);
        Persona saved = service.save(model);

        if (saved == null || saved.getId() == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No se pudo guardar la persona")
                    .build();
        }

        PersonaDto savedDto = mapper.toDto(saved);

        return Response.ok(savedDto)
                .entity(savedDto)
                .build();

    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {

        service.findById(id)
                .map(mapper::toDto)
                .orElseThrow(
                        () -> new NotFoundException("Persona con ID " + id + " no encontrada", "PersonaResource.class")
                );

        service.deleteById(id);
        return Response.ok("Persona eliminada con éxito").build();

    }

}
