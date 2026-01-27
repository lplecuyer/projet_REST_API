package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.Models.Vendeur;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


@Path("/sellers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VendeurResource {

    @POST
    public Response create(@Valid Vendeur vendeur) {
        return Response.status(Response.Status.CREATED).entity(vendeur).build();
    }
}
