package ca.ulaval.glo2003.Models;

import jakarta.validation.Valid;

import java.time.LocalDate;

public class ProductRecordCreator {


    public static ProductRecord makeProductRecord(@Valid Product product) {
        int id = product.getId();
        String title = product.getTitle();
        String description = product.getDescription();
        String category = product.getCategory();
        float suggestedPrice = product.getSuggestedPrice();
        LocalDate createdAt = product.getCreatedAt();
        SellerView seller =  new SellerView(product.getSeller().getId(),
                                            product.getSeller().getName());
        OfferView offers = new OfferView(product.getOfferCount(),
                                        product.getAvgOfferAmount());
        return new ProductRecord(id, title, description, category,
                                suggestedPrice, createdAt, seller, offers);
    }
    public record ProductRecord(int id, String title, String description, String category, float suggestedPrice,
                                LocalDate createdAt, SellerView seller, OfferView offers){}
    public record SellerView(int id, String name) { }

    public record OfferView(int count, Float avgAmount) { }
}
