package ca.ulaval.glo2003.Models;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

//Classe qui permet de créer un ProductRecord (DTO) pour l'envoi des requêtes
public final class ProductRecordFactory {

    private ProductRecordFactory(){}

    //Crée un ProductRecord à partir d'un Product valide
    public static ProductRecord makeProductRecord(@Valid Product product) {
        String id = product.getId();
        String title = product.getTitle();
        String description = product.getDescription();
        String category = product.getCategory().toString();
        float suggestedPrice = product.getSuggestedPrice();
        LocalDateTime createdAt = product.getCreatedAt();
        SellerView seller =  new SellerView(product.getSellerId(), product.getSeller().getName());
        OfferView offers = new OfferView(product.getOfferCount(), product.getAvgOfferAmount());
        return new ProductRecord(id, title, description, category, suggestedPrice, createdAt, seller, offers);
    }
    //Record (Classe DTO) pour la classe Product
    public record ProductRecord(String id, String title, String description, String category, float suggestedPrice,
                                LocalDateTime createdAt, SellerView seller, OfferView offers){}

    public record ProductsResponse(java.util.Collection<ca.ulaval.glo2003.Models.ProductRecordFactory.ProductRecord> products) {}

    //Record destiné uniquement pour le ProductRecord
    public record SellerView(String id, String name) { }

    //Record destiné uniquement pour le ProductRecord
    public record OfferView(int count, @JsonbNillable Float avgAmount) { }
}
