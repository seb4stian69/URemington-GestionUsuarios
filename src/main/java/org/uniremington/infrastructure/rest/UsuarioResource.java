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
import org.uniremington.application.dto.LoginDto;
import org.uniremington.application.dto.ResetPasswordDto;
import org.uniremington.application.dto.UsuarioDto;
import org.uniremington.application.service.MailerService;
import org.uniremington.application.service.UsuarioService;
import org.uniremington.domain.model.Usuario;
import org.uniremington.shared.base.BaseError;
import org.uniremington.shared.base.BaseRequest;
import org.uniremington.shared.base.BaseResponse;
import org.uniremington.shared.exception.MicroServiceException;
import org.uniremington.shared.exception.NotFoundException;
import org.uniremington.shared.util.UsuarioMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/usuario")
public class UsuarioResource {

    private static final String NAMECLASS = "UsuarioResource.class";
    private static final String RESPONSE = "response";

    UsuarioService service;
    MailerService mailerService;
    UsuarioMapper mapper;

    @Inject UsuarioResource(UsuarioService service, UsuarioMapper mapper, MailerService mailerService){
        this.service = service;
        this.mailerService = mailerService;
        this.mapper = mapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        BaseResponse<List<Usuario>> response;

        try {
            List<Usuario> usuarios = service.findAll();
            response = new BaseResponse<>(null, usuarios, new BaseError());
            return Response.ok(response).build();
        } catch (MicroServiceException ex) {
            response = new BaseResponse<>(null, ex.getBaseError());
            return Response.status(ex.getCodigoHttp()).entity(response).build();
        } catch (Exception ex) {
            BaseError error = MicroServiceException.procesar(ex, NAMECLASS);
            response = new BaseResponse<>(null, error);
            return Response.serverError().entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        BaseResponse<Usuario> response;

        try {
            Usuario usuario = service.findById(id)
                    .orElseThrow(() -> new NotFoundException("Usuario con ID " + id + " no encontrado", NAMECLASS));

            response = new BaseResponse<>(null, usuario, new BaseError());
            return Response.ok(response).build();
        } catch (MicroServiceException ex) {
            response = new BaseResponse<>(null, ex.getBaseError());
            return Response.status(ex.getCodigoHttp()).entity(response).build();
        } catch (Exception ex) {
            BaseError error = MicroServiceException.procesar(ex, NAMECLASS);
            response = new BaseResponse<>(null, error);
            return Response.serverError().entity(response).build();
        }

    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(BaseRequest<LoginDto> request) {

        Usuario requestUser = new Usuario();
        requestUser.setNombreUsuario(request.getBody().getUsername());
        requestUser.setContrasena(request.getBody().getPassword());
        requestUser.setSalt(request.getBody().getSalt());

        BaseResponse<Map<String, String>> response;

        try {
            if (service.login(requestUser)) {
                Usuario getUserData = service.getByUsername(request.getBody().getUsername());
                Map<String, String> body = new HashMap<>();
                body.put(RESPONSE, "Login exitoso");
                body.put("perfil", getUserData.getIdPerfil());
                response = new BaseResponse<>(request.getHeader(), body, new BaseError());
                return Response.ok(response).build();
            } else {
                throw new NotFoundException("Credenciales inválidas.", NAMECLASS);
            }

        } catch (MicroServiceException ex) {
            response = new BaseResponse<>(request.getHeader(), ex.getBaseError());
            return Response.status(ex.getCodigoHttp()).entity(response).build();
        } catch (Exception ex) {
            BaseError baseError = MicroServiceException.procesar(ex, NAMECLASS);
            response = new BaseResponse<>(request.getHeader(), baseError);
            return Response.serverError().entity(response).build();
        }

    }

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(BaseRequest<UsuarioDto> request) {

        BaseResponse<UsuarioDto> response;
        UsuarioDto dto = request.getBody();

        try {
            if (dto.getIdPersona() == null) {
                BaseError error = new BaseError("400", "El ID de la persona es obligatorio", NAMECLASS);
                response = new BaseResponse<>(null, error);
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            Usuario model = mapper.toModel(dto);
            model.setId(dto.getIdPersona());

            Usuario saved = service.save(model);
            if (saved == null || saved.getId() == null) {
                BaseError error = new BaseError("500", "No se pudo guardar el usuario", NAMECLASS);
                response = new BaseResponse<>(null, error);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
            }

            UsuarioDto savedDto = mapper.toDto(saved);
            response = new BaseResponse<>(null, savedDto, new BaseError());
            return Response.ok(response).build();

        } catch (Exception ex) {
            BaseError error = MicroServiceException.procesar(ex, NAMECLASS);
            response = new BaseResponse<>(null, error);
            return Response.serverError().entity(response).build();
        }

    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {

        BaseResponse<Map<String, String>> response;

        try {
            Usuario usuario = service.findById(id)
                    .orElseThrow(() -> new NotFoundException("Usuario con ID " + id + " no encontrado", NAMECLASS));

            service.deleteById(usuario.getId());

            Map<String, String> body = new HashMap<>();
            body.put(RESPONSE, "Usuario eliminado con éxito");

            response = new BaseResponse<>(null, body, new BaseError());
            return Response.ok(response).build();

        } catch (MicroServiceException ex) {
            response = new BaseResponse<>(null, ex.getBaseError());
            return Response.status(ex.getCodigoHttp()).entity(response).build();
        } catch (Exception ex) {
            BaseError error = MicroServiceException.procesar(ex, NAMECLASS);
            response = new BaseResponse<>(null, error);
            return Response.serverError().entity(response).build();
        }
    }

    @POST
    @Path("/reset-password")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMail(BaseRequest<ResetPasswordDto> request) {
        BaseResponse<Map<String, String>> response;
        ResetPasswordDto body = request.getBody();


        try {
            Map<String, Object> result = service.resetPassword(body.getUsername());

            Usuario usuario = (Usuario) result.get("user");

            this.mailerService.sendReset(
                (String) result.get("mail"),
                usuario.getNombreUsuario(),
                (String) result.get("pass")
            );

            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put(RESPONSE,"Cambio exitoso!");

            response = new BaseResponse<>(null, bodyMap, new BaseError());
            return Response.ok(response).build();

        } catch (MicroServiceException ex) {
            response = new BaseResponse<>(null, ex.getBaseError());
            return Response.status(ex.getCodigoHttp()).entity(response).build();
        } catch (Exception ex) {
            BaseError error = MicroServiceException.procesar(ex, NAMECLASS);
            response = new BaseResponse<>(null, error);
            return Response.serverError().entity(response).build();
        }

    }

}
