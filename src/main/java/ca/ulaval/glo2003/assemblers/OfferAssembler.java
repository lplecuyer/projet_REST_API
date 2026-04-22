package ca.ulaval.glo2003.assemblers;

import ca.ulaval.glo2003.models.Offer;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

public final class OfferAssembler {

    private OfferAssembler() { }

    public static OfferResponse makeOfferResponse(@Valid Offer offer) {
        String offerId = offer.getOfferId();
        String productId = offer.getProductId();
        Float amount = offer.getAmount();
        String message = offer.getMessage();
        String buyerUsername = offer.getBuyerUsername();
        LocalDateTime offerCreatedDate = offer.getCreatedAt();
        return new OfferResponse(offerId, productId, amount, message, buyerUsername, offerCreatedDate);
    }

    public record OfferRequest(Float amount, String message){}

    public record OfferResponse(String offerId, String productId, Float amount, String message, String buyerUsername,
                                LocalDateTime offerCreatedDate) {
    }
}
