package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.Models.Product;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @POST
    public Response create(@Valid Product product, @Context UriInfo uriInfo){
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
        return Response.created(location).entity(product).build();
    }
}
