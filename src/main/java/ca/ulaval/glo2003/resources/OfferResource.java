package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.services.OfferService;
import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.assemblers.OfferAssembler;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.Objects;

@Path("/products/{productId}/offers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OfferResource {

  private final OfferService offerService = new OfferService();

  public OfferResource() {
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

    ProductAssembler.ProductResponse productResponse = offerService.getProduct(productId);
    if (productResponse == null) {
      return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(
          "NOT_FOUND", "Product not found")).build();

    } else if (offerRequest.amount() < productResponse.suggestedPrice()) {
      return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(
          "INVALID_PARAMETER", "Amount must be larger than suggested amount")).build();

    } else if (offerService.offerForSameProductAndUserAlreadyExists(buyerUsername, productId)) {
      return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse(
          "ALREADY_EXISTS", "Offer already exists for this buyer and product")).build();
    }

    if(Objects.equals(offerService.getProduct(productId).saleStatus(), "sold")) {
      return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse(
          "NOT_PERMITTED", "Given product is already sold")).build();
    }

    OfferAssembler.OfferResponse offerResponse = offerService.addOffer(offerRequest, productId, buyerUsername);
    URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(offerResponse.offerId())).build();
    return Response.created(location).entity(offerResponse).build();
  }
}