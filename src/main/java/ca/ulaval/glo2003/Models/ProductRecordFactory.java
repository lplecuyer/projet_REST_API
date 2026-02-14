package ca.ulaval.glo2003.Models;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.Valid;

import java.time.LocalDate;

//Classe qui permet de créer un ProductRecord (DTO) pour l'envoi des requêtes
public final class ProductRecordFactory {

    private ProductRecordFactory(){}

    //Crée un ProductRecord à partir d'un Product valide
    public static ProductRecord makeProductRecord(@Valid Product product) {
        int id = product.getId();
        String title = product.getTitle();
        String description = product.getDescription();
        String category = product.getCategory();
        float suggestedPrice = product.getSuggestedPrice();
        LocalDate createdAt = product.getCreatedAt();
        SellerView seller =  new SellerView(product.getSeller().getId(), product.getSeller().getName());
        OfferView offers = new OfferView(product.getOfferCount(), product.getAvgOfferAmount());
        return new ProductRecord(id, title, description, category, suggestedPrice, createdAt, seller, offers);
    }
    //Record (Classe DTO) pour la classe Product
    public record ProductRecord(int id, String title, String description, String category, float suggestedPrice,
                                LocalDate createdAt, SellerView seller, OfferView offers){}
    //Record destiné uniquement pour le ProductRecord
    public record SellerView(int id, String name) { }

    //Record destiné uniquement pour le ProductRecord
    public record OfferView(int count, @JsonbNillable Float avgAmount) { }
}
