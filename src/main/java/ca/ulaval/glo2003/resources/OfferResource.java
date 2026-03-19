package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.ServerApplication;
import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.models.*;
import ca.ulaval.glo2003.assemblers.OfferAssembler;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;

@Path("/products/{productId}/offers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OfferResource {

  private final ServerApplication serverApplication;

  public OfferResource(ServerApplication serverApplication){
    this.serverApplication = serverApplication;
  }

  @POST
  public Response create(OfferAssembler.OfferRequest offerRequest,
                         @PathParam("productId") String productId,
                         @Context UriInfo uriInfo,
                         @HeaderParam("X-Buyer-Username") String buyerUsername) {

    if (productId == null) {
      return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(
          "MISSING_PARAMETER", "productId is mandatory")).build();
    }

    ProductAssembler.ProductResponse productResponse = serverApplication.getProduct(productId);
    if (productResponse == null) {
      return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(
          "NOT_FOUND", "Product not found")).build();

    } else if (offerRequest.amount() < productResponse.suggestedPrice()) {
      return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(
          "INVALID_PARAMETER", "Amount must be larger than suggested amount")).build();

    }

    else if (serverApplication.offerForSameProductAndUserAlreadyExists(buyerUsername, productId)) {
      return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse(
          "ALREADY_EXISTS", "Offer already exists for this buyer and product")).build();
    }

    OfferAssembler.OfferResponse offerResponse = serverApplication.addOffer(offerRequest, productId, buyerUsername);
    URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(offerResponse.offerId())).build();
    return Response.created(location).entity(offerResponse).build();
  }
}