package ca.ulaval.glo2003.Models;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

public final class ProductResponseFactory {

    private ProductResponseFactory(){}

    public static ProductResponse makeProductResponse(@Valid Product product) {
        String id = product.getId();
        String title = product.getTitle();
        String description = product.getDescription();
        String category = product.getCategory();
        float suggestedPrice = product.getSuggestedPrice();
        LocalDateTime createdAt = product.getCreatedAt();
        PartialSellerResponse seller =  new PartialSellerResponse(product.getSeller().getId(), product.getSeller().getName());
        PartialOfferResponse offers = new PartialOfferResponse(product.getOfferCount(), product.getAvgOfferAmount());
        return new ProductResponse(id, title, description, category, suggestedPrice, createdAt, seller, offers);
    }

    public record ProductResponse(String id, String title, String description, String category, float suggestedPrice,
                                  LocalDateTime createdAt, PartialSellerResponse seller, PartialOfferResponse offers){}

    public record PartialSellerResponse(String id, String name) { }

    public record PartialOfferResponse(int count, @JsonbNillable Float avgAmount) { }
}
