package ca.ulaval.glo2003.Models;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.Valid;

import java.time.LocalDate;

public final class ProductResponseFactory {

    private ProductResponseFactory(){}

    public static ProductResponse makeProductResponse(@Valid Product product) {
        int id = product.getId();
        String title = product.getTitle();
        String description = product.getDescription();
        String category = product.getCategory();
        float suggestedPrice = product.getSuggestedPrice();
        LocalDate createdAt = product.getCreatedAt();
        PartialSellerResponse seller =  new PartialSellerResponse(product.getSeller().getId(), product.getSeller().getName());
        PartialOfferResponse offers = new PartialOfferResponse(product.getOfferCount(), product.getAvgOfferAmount());
        return new ProductResponse(id, title, description, category, suggestedPrice, createdAt, seller, offers);
    }

    public record ProductResponse(int id, String title, String description, String category, float suggestedPrice,
                                  LocalDate createdAt, PartialSellerResponse seller, PartialOfferResponse offers){}

    public record PartialSellerResponse(int id, String name) { }

    public record PartialOfferResponse(int count, @JsonbNillable Float avgAmount) { }
}
