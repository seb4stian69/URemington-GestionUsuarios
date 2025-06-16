package org.uniremington.infrastructure.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uniremington.infrastructure.entity.ProductoEntity;
import org.uniremington.infrastructure.entity.ProductoEntity;
import org.uniremington.infrastructure.repository.JpaProductoRepository;
import org.uniremington.infrastructure.repository.trash.AgregarProductoRequest;
import org.uniremington.infrastructure.repository.trash.CarritoDTO;
import org.uniremington.shared.base.BaseError;
import org.uniremington.shared.base.BaseHeader;
import org.uniremington.shared.base.BaseRequest;
import org.uniremington.shared.base.BaseResponse;
import org.uniremington.shared.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Path("/producto")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductoResource {

    @Inject
    JpaProductoRepository repository;


    @GET
    @Path("/todos/{idProductor}")
    public Response findAll(@PathParam("idProductor") Long idProductor) {
        BaseResponse<List<ProductoEntity>> response = new BaseResponse<>();
        try {
            List<ProductoEntity> data = repository.findAll(idProductor)
                    .stream()
                    .toList();

            response.setBody(data);
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error al obtener productos", ProductoResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/{codprod}")
    public Response findById(@PathParam("codprod") String codprod) {
        BaseResponse<ProductoEntity> response = new BaseResponse<>();
        try {
            ProductoEntity dto = repository.findById(codprod)
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado", ProductoResource.class.getSimpleName()));

            response.setBody(dto);
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error al buscar producto", ProductoResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @PermitAll
    public Response create(BaseRequest<ProductoEntity> request) {
        BaseResponse<ProductoEntity> response = new BaseResponse<>();
        try {
            ProductoEntity saved = repository.save(request.getBody());

            response.setBody(saved);
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error al guardar producto", ProductoResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @PUT
    @Path("/{codprod}")
    public Response update(@PathParam("codprod") String codprod, BaseRequest<ProductoEntity> request) {
        BaseResponse<ProductoEntity> response = new BaseResponse<>();
        try {
            ProductoEntity dto = request.getBody();
            dto.setCodprod(codprod); // Asegura el ID

            ProductoEntity updated = repository.update(request.getBody());
            response.setBody(updated);
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error al actualizar producto", ProductoResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Path("/{codprod}")
    public Response delete(@PathParam("codprod") String codprod) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            repository.delete(codprod);

            response.setBody("Producto eliminado con éxito");
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error al eliminar producto", ProductoResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/carrito/{idUser}")
    public Response obtenerCarrito(@PathParam("idUser") Long idUser) {
        BaseResponse<CarritoDTO> response = new BaseResponse<>();
        try {
            CarritoDTO carrito = repository.obtenerCarritoConDetalles(idUser);

            response.setBody(carrito);
            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error al obtener carrito", ProductoResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @Path("/carrito/agregar-producto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregarProductoAlCarrito(BaseRequest<AgregarProductoRequest> request) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            repository.agregarProductoAlCarrito(
                request.getBody().getIdConsumidor(),
                request.getBody().getIdProducto(),
                request.getBody().getCantidad()
            );

            response.setHeader(new BaseHeader());
            response.setError(new BaseError());

            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            response.setError(new BaseError("500", "Error al agregar producto al carrito", ProductoResource.class.getSimpleName()));
            response.setHeader(new BaseHeader());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }


}
