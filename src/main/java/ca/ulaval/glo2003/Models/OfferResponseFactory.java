package ca.ulaval.glo2003.Models;

import jakarta.validation.Valid;

import java.time.LocalDate;

public final class OfferResponseFactory {

    private OfferResponseFactory() { }

    public static OfferResponse makeOfferResponse(@Valid Offer offer) {
        int offerId = offer.getOfferId();
        String productId = offer.getProductId();
        double amount = offer.getAmount();
        String message = offer.getMessage();
        String buyerUsername = offer.getBuyerUsername();
        LocalDate offerCreatedDate = offer.getCreatedAt();
        return new OfferResponse(offerId, productId, amount, message, buyerUsername, offerCreatedDate);
    }

    public record OfferResponse(int offerId, String productId, double amount, String message, String buyerUsername,
                                LocalDate offerCreatedDate) {
    }
}
