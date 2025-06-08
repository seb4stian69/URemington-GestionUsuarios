package org.uniremington.infrastructure.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uniremington.application.dto.PersonaDto;
import org.uniremington.application.service.PersonaService;
import org.uniremington.domain.model.Persona;
import org.uniremington.shared.base.BaseError;
import org.uniremington.shared.base.BaseHeader;
import org.uniremington.shared.base.BaseRequest;
import org.uniremington.shared.base.BaseResponse;
import org.uniremington.shared.exception.MicroServiceException;
import org.uniremington.shared.exception.NotFoundException;
import org.uniremington.shared.exception.RepoException;
import org.uniremington.shared.util.PersonaMapper;

import java.util.List;

@Path("/personas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonaResource {

    PersonaService service;
    PersonaMapper mapper;

    @Inject PersonaResource(PersonaService service, PersonaMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }

    @GET
    public Response findAll() {
        BaseResponse<List<PersonaDto>> response = new BaseResponse<>();
        try {
            List<PersonaDto> data = service.findAll().stream()
                    .map(mapper::toDto)
                    .toList();

            response.setBody(data);
            response.setError(new BaseError());
            response.setHeader(new BaseHeader());

            return Response.ok(response).build();

        } catch (MicroServiceException e) {
            response.setError(e.getBaseError());
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error inesperado al obtener personas", PersonaResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        BaseResponse<PersonaDto> response = new BaseResponse<>();
        try {
            PersonaDto dto = service.findById(id)
                    .map(mapper::toDto)
                    .orElseThrow(() -> new NotFoundException("Persona con ID " + id + " no encontrada", PersonaResource.class.getSimpleName()));

            response.setBody(dto);
            response.setError(new BaseError());
            response.setHeader(new BaseHeader());
            return Response.ok(response).build();

        } catch (MicroServiceException e) {
            response.setError(e.getBaseError());
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error inesperado al buscar persona", PersonaResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @PermitAll
    public Response create(BaseRequest<PersonaDto> request) {
        BaseResponse<PersonaDto> response = new BaseResponse<>();
        try {
            Persona model = mapper.toModel(request.getBody());
            Persona saved = service.save(model);

            if (saved == null || saved.getId() == null) {
                throw new RepoException("No se pudo guardar la persona", PersonaResource.class.getSimpleName());
            }

            PersonaDto dto = mapper.toDto(saved);
            response.setBody(dto);
            response.setError(new BaseError());
            response.setHeader(new BaseHeader());
            return Response.ok(response).build();

        } catch (MicroServiceException e) {
            response.setError(e.getBaseError());
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error inesperado al crear persona", PersonaResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            Persona persona = service.findById(id)
                    .orElseThrow(() -> new NotFoundException("Persona con ID " + id + " no encontrada", PersonaResource.class.getSimpleName()) {
                    });

            service.deleteById(persona.getId());

            response.setBody("Persona eliminada con éxito");
            response.setError(new BaseError());
            response.setHeader(new BaseHeader());
            return Response.ok(response).build();

        } catch (MicroServiceException e) {
            response.setError(e.getBaseError());
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error inesperado al eliminar persona", PersonaResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

}