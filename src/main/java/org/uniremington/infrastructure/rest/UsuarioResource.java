package org.uniremington.infrastructure.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uniremington.application.dto.LoginDto;
import org.uniremington.application.dto.ResetPasswordDto;
import org.uniremington.application.dto.UsuarioDto;
import org.uniremington.application.service.UsuarioService;
import org.uniremington.domain.model.Usuario;
import org.uniremington.shared.exception.NotFoundException;
import org.uniremington.shared.util.ApiResponse;
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

        List<Usuario> result = service.findAll().stream()
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

        Usuario persona = service.findById(id)
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

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDto dto) {

        Usuario queryUser = new Usuario();
        queryUser.setNombreUsuario(dto.getUsername());
        queryUser.setContrasena(dto.getPassword());
        queryUser.setSalt(dto.getSalt());

        if(service.login(queryUser)){

            ObjectMapper mapper = new ObjectMapper();
            String json;

            try {
                json = mapper.writeValueAsString("Login Exitoso");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return Response.ok(json).build();

        }

        return Response.serverError().build();

    }

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(UsuarioDto dto) {
        try {
            if (dto.getIdPersona() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El ID de la persona es obligatorio")
                        .build();
            }

            // Crear modelo con el ID de la persona
            Usuario model = mapper.toModel(dto);
            model.setId(dto.getIdPersona()); // ID del usuario es el mismo que el de la persona

            Usuario saved = service.save(model);

            if (saved == null || saved.getId() == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("No se pudo guardar el usuario")
                        .build();
            }

            UsuarioDto savedDto = mapper.toDto(saved);
            return Response.ok(savedDto).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al guardar el usuario: " + e.getMessage())
                    .build();
        }
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


    @POST
    @Path("/reset-password")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMail(ResetPasswordDto body) {

        ApiResponse result = service.resetPassword(body.getUsername()   );

        if (result.getMessage().equalsIgnoreCase("Usuario no encontrado.")
                || result.getMessage().contains("no tiene un correo")) {
            return Response.status(Response.Status.NOT_FOUND).entity(result).build();
        }

        if (!result.isSuccess()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        return Response.ok(result).build();

    }


}
