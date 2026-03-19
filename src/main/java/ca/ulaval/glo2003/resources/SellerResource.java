package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.assemblers.SellerAssembler;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;


@Path("/sellers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SellerResource {

  private final ISellerRepository sellerRepository;

  @Inject
  public SellerResource(ISellerRepository sellers) {
    this.sellerRepository = sellers;
  }

  @POST
  public Response create(@Valid Seller seller, @Context UriInfo uriInfo) {
    sellerRepository.save(seller);
    URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(seller.getId())).build();
    return Response.created(location).entity(seller).build();
  }

  @Path("/{id}")
  @GET
  public Response getSeller(@PathParam("id") String id) {
    Seller seller = sellerRepository.get(id);

    if (seller == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(new ErrorResponse("NOT_FOUND", "Seller with id " + id + " not found"))
          .build();
    }

    return Response.status(Response.Status.OK)
        .entity(SellerAssembler.makeSellerResponse(seller))
        .build();
  }
}