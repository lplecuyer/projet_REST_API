package ca.ulaval.glo2003.Resources;

import ca.ulaval.glo2003.ExceptionHandlers.ErrorResponse;
import ca.ulaval.glo2003.Models.*;
import ca.ulaval.glo2003.Models.Offer;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;

@Path("/products/{productId}/offers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OfferResource {

    @POST
    public Response create(@Valid Offer offer, @PathParam("productId") String productId, @Context UriInfo uriInfo, @HeaderParam("X-Buyer-Username") String buyerUsername) {
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(offer.getOfferId())).build();

        if (productId == null || buyerUsername == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("MISSING_PARAMETER", "Required parameter is missing")).build();
        }

        Product product = ProductRepository.INSTANCE.get(productId);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("NOT_FOUND", "Product not found")).build();

        } else if (offer.getAmount() < product.getSuggestedPrice()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("INVALID_PARAMETER", "Amount must be larger than suggested amount")).build();

        } else if (offerForSameProductAndUserAlreadyExists(ProductRepository.INSTANCE.get(productId), buyerUsername)) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse("ALREADY_EXISTS", "Offer already exists for this buyer and product")).build();
        }
        registerOffer(offer, product, buyerUsername);
        return Response.created(location).entity(OfferResponseFactory.makeOfferResponse(offer)).build();
    }

    public boolean offerForSameProductAndUserAlreadyExists(Product product, String buyerUsername) {
        boolean alreadyExists = false;
        for (Offer offer : product.getOffers()) {
            alreadyExists = offer.getBuyerUsername().equals(buyerUsername);
        }
        return alreadyExists;
    }

    private void registerOffer(Offer offer, Product product, String buyerUsername) {
        offer.setProductId(String.valueOf(product.getId()));
        offer.setBuyerUsername(buyerUsername);
        product.addOffer(offer);
    }
}
