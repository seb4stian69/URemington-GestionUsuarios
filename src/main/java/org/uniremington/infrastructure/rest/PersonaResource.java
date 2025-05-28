package org.uniremington.infrastructure.rest;

import org.uniremington.application.dto.PersonaDto;
import org.uniremington.application.service.PersonaService;
import org.uniremington.shared.util.PersonaMapper;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/personas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonaResource {

    @Inject /*->*/ PersonaService personaService;
    @Inject /*->*/ PersonaMapper personaMapper;

    @GET
    public List<PersonaDto> findAll() {
        return personaService.findAll().stream()
                .map(personaMapper::toDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public PersonaDto findById(@PathParam("id") Long id) {
        return personaMapper.toDto(personaService.findById(id));
    }

    @POST
    public PersonaDto create(PersonaDto dto) {
        return personaMapper.toDto(personaService.save(personaMapper.toModel(dto)));
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        personaService.deleteById(id);
    }

}
