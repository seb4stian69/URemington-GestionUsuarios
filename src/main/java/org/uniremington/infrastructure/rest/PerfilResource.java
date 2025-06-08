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
import org.uniremington.application.dto.PerfilDto;
import org.uniremington.application.service.PerfilService;
import org.uniremington.domain.model.Perfil;
import org.uniremington.shared.base.BaseError;
import org.uniremington.shared.base.BaseHeader;
import org.uniremington.shared.base.BaseRequest;
import org.uniremington.shared.base.BaseResponse;
import org.uniremington.shared.exception.MicroServiceException;
import org.uniremington.shared.exception.NotFoundException;
import org.uniremington.shared.exception.RepoException;
import org.uniremington.shared.util.PerfilMapper;

import java.util.List;

@Path("/perfil")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PerfilResource {

    PerfilService service;
    PerfilMapper mapper;

    @Inject PerfilResource(PerfilService service, PerfilMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GET
    public Response findAll() {
        BaseResponse<List<PerfilDto>> response = new BaseResponse<>();
        try {
            List<PerfilDto> data = service.findAll().stream()
                    .map(mapper::toDto)
                    .toList();

            response.setBody(data);
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();

        } catch (MicroServiceException e) {
            response.setError(e.getBaseError());
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error inesperado al obtener perfiles", PerfilResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        BaseResponse<PerfilDto> response = new BaseResponse<>();
        try {
            PerfilDto dto = service.findById(id)
                    .map(mapper::toDto)
                    .orElseThrow(() ->
                            new NotFoundException("Perfil con ID " + id + " no encontrada", PerfilResource.class.getSimpleName())
                    );

            response.setBody(dto);
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();

        } catch (MicroServiceException e) {
            response.setError(e.getBaseError());
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error inesperado al buscar perfil", PerfilResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @PermitAll
    public Response create(BaseRequest<PerfilDto> request) {
        BaseResponse<PerfilDto> response = new BaseResponse<>();
        try {
            Perfil model = mapper.toModel(request.getBody());
            Perfil saved = service.save(model);

            if (saved == null || saved.getId() == null) {
                throw new RepoException("No se pudo guardar el perfil", PerfilResource.class.getSimpleName());
            }

            PerfilDto dto = mapper.toDto(saved);
            response.setBody(dto);
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();

        } catch (MicroServiceException e) {
            response.setError(e.getBaseError());
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error inesperado al crear perfil", PerfilResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            Perfil perfil = service.findById(id)
                    .orElseThrow(() ->
                            new NotFoundException("Perfil con ID " + id + " no encontrada", PerfilResource.class.getSimpleName())
                    );

            service.deleteById(perfil.getId());

            response.setBody("Perfil eliminado con éxito");
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();

        } catch (MicroServiceException e) {
            response.setError(e.getBaseError());
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error inesperado al eliminar perfil", PerfilResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}