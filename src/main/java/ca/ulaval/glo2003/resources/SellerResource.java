package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.services.SellerService;
import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.assemblers.SellerAssembler;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;


@Path("/sellers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SellerResource {

  private SellerService sellerService = new SellerService();

  public SellerResource() {}

  @POST
  public Response create(SellerAssembler.SellerRequest sellerRequest, @Context UriInfo uriInfo) {
    SellerAssembler.SellerResponse sellerResponse = sellerService.addSeller(sellerRequest);
    URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(sellerResponse.id())).build();
    return Response.created(location).entity(sellerResponse).build();
  }

  @Path("/{id}")
  @GET
  public Response getSeller(@PathParam("id") String id) {
    SellerAssembler.SellerResponse sellerResponse = sellerService.getSeller(id);

    if (sellerResponse == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(new ErrorResponse("NOT_FOUND", "Seller with id " + id + " not found"))
          .build();
    }

    return Response.status(Response.Status.OK).entity(sellerResponse).build();
  }

  public void setSellerService(SellerService sellerService){
    this.sellerService = sellerService;
  }
}