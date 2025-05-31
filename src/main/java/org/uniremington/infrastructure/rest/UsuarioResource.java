package org.uniremington.infrastructure.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uniremington.application.dto.UsuarioDto;
import org.uniremington.application.service.UsuarioService;
import org.uniremington.domain.model.Usuario;
import org.uniremington.shared.exception.NotFoundException;
import org.uniremington.shared.util.UsuarioMapper;

import java.util.List;
import java.util.stream.Collectors;

@Path("/usuario")
public class UsuarioResource {
    @Inject /*->*/ UsuarioService service;
    @Inject /*->*/ UsuarioMapper mapper;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {

        List<UsuarioDto> result = service.findAll().stream()
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

        UsuarioDto persona = service.findById(id)
            .map(mapper::toDto)
            .orElseThrow(
                () -> new NotFoundException("Usuario con ID " + id + " no encontrada")
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

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String user, String contrasena) {

        service.login(user, contrasena)
            .map(mapper::toDto)
            .orElseThrow(
                () -> new NotFoundException("Nombre de usuario o contraseña incorrectos.")
            );

        ObjectMapper mapper = new ObjectMapper();
        String json;

        try {
            json = mapper.writeValueAsString("ok");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Response.ok(json).build();

    }

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(UsuarioDto dto) {

        Usuario model = mapper.toModel(dto);
        Usuario saved = service.save(model);

        if (saved == null || saved.getId() == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No se pudo guardar la persona")
                    .build();
        }

        UsuarioDto savedDto = mapper.toDto(saved);

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
                        () -> new NotFoundException("Usuario con ID " + id + " no encontrada")
                );

        service.deleteById(id);
        return Response.ok("Usuario eliminada con éxito").build();

    }

}
