package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.application.ServerApplication;
import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.assemblers.SellerAssembler;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;


@Path("/sellers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SellerResource {

  private final ServerApplication serverApplication;

  public SellerResource(ServerApplication serverApplication) {
    this.serverApplication = serverApplication;
  }

  @POST
  public Response create(SellerAssembler.SellerRequest sellerRequest, @Context UriInfo uriInfo) {
    SellerAssembler.SellerResponse sellerResponse = serverApplication.addSeller(sellerRequest);
    URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(sellerResponse.id())).build();
    return Response.created(location).entity(sellerResponse).build();
  }

  @Path("/{id}")
  @GET
  public Response getSeller(@PathParam("id") String id) {
    SellerAssembler.SellerResponse sellerResponse = serverApplication.getSeller(id);

    if (sellerResponse == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(new ErrorResponse("NOT_FOUND", "Seller with id " + id + " not found"))
          .build();
    }

    return Response.status(Response.Status.OK).entity(sellerResponse).build();
  }
}