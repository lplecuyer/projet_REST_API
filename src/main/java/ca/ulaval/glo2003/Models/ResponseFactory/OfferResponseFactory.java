package ca.ulaval.glo2003.Models.ResponseFactory;

import ca.ulaval.glo2003.Models.Offer;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

public final class OfferResponseFactory {

    private OfferResponseFactory() { }

    public static OfferResponse makeOfferResponse(@Valid Offer offer) {
        String offerId = offer.getOfferId();
        String productId = offer.getProductId();
        double amount = offer.getAmount();
        String message = offer.getMessage();
        String buyerUsername = offer.getBuyerUsername();
        LocalDateTime offerCreatedDate = offer.getCreatedAt();
        return new OfferResponse(offerId, productId, amount, message, buyerUsername, offerCreatedDate);
    }

    public record OfferResponse(String offerId, String productId, double amount, String message, String buyerUsername,
                                LocalDateTime offerCreatedDate) {
    }
}
