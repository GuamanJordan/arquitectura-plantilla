package ec.edu.arquitectura.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoResource {
    private final ProductoRepository repository = ProductoRepository.getInstance();

    @GET
    public List<Producto> listar() {
        return repository.listar();
    }

    @GET
    @Path("/{id}")
    public Producto buscar(@PathParam("id") long id) {
        return repository.buscar(id).orElseThrow(NotFoundException::new);
    }

    @POST
    public Response crear(Producto producto) {
        Producto creado = repository.crear(producto);
        return Response.created(URI.create("/api/productos/" + creado.id)).entity(creado).build();
    }

    @PUT
    @Path("/{id}")
    public Producto actualizar(@PathParam("id") long id, Producto producto) {
        return repository.actualizar(id, producto).orElseThrow(NotFoundException::new);
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") long id) {
        if (!repository.eliminar(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }
}
