package org.uniremington.infrastructure.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uniremington.application.dto.PerfilDto;
import org.uniremington.application.service.PerfilService;
import org.uniremington.domain.model.Perfil;
import org.uniremington.shared.exception.NotFoundException;
import org.uniremington.shared.util.PerfilMapper;

import java.util.List;
import java.util.stream.Collectors;

@Path("/perfil")
public class PerfilResource {

    @Inject /*->*/ PerfilService service;
    @Inject /*->*/ PerfilMapper mapper;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {

        List<PerfilDto> result = service.findAll().stream()
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

        PerfilDto perfil = service.findById(id)
            .map(mapper::toDto)
            .orElseThrow(
                () -> new NotFoundException("Perfil con ID " + id + " no encontrada")
            );

        ObjectMapper mapper = new ObjectMapper();
        String json;

        try {
            json = mapper.writeValueAsString(perfil);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Response.ok(json).build();

    }

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PerfilDto dto) {

        Perfil model = mapper.toModel(dto);
        Perfil saved = service.save(model);

        if (saved == null || saved.getId() == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No se pudo guardar la perfil")
                    .build();
        }

        PerfilDto savedDto = mapper.toDto(saved);

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
                        () -> new NotFoundException("Perfil con ID " + id + " no encontrada")
                );

        service.deleteById(id);
        return Response.ok("Perfil eliminada con éxito").build();

    }

}
