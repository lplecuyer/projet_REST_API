package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.Models.Seller;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;


@Path("/sellers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SellerResource {


    // cree un vendeur et renvoie ses attributs avec le header contenant son id
    @POST
    public Response create(@Valid Seller seller, @Context UriInfo uriInfo) {
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(seller.getId())).build();
        return Response.created(location).entity(seller).build();
    }
}
