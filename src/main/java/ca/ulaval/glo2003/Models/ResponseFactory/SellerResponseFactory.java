package ca.ulaval.glo2003.Models.ResponseFactory;

import ca.ulaval.glo2003.Models.Offer;
import ca.ulaval.glo2003.Models.Product;
import ca.ulaval.glo2003.Models.ProductCategory;
import ca.ulaval.glo2003.Models.Seller;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public final class SellerResponseFactory {

    private SellerResponseFactory() {}

    public static SellerResponse makeSellerResponse(@Valid Seller seller) {
        String id = seller.getId();
        String createdAt = seller.getCreatedAt().toString();
        String name = seller.getName();
        String birthdate = seller.getBirthdate().toString();
        String email = seller.getEmail();
        String phoneNumber = seller.getPhoneNumber();
        String bio = seller.getBio();
        Collection<SellerResponseFactory.PartialProductResponse> products = makePartialProductResponse(seller);

        return new SellerResponse(id, createdAt, name, birthdate, email, phoneNumber, bio, products);
    }

    public static Collection<PartialProductResponse> makePartialProductResponse(Seller seller) {
        Collection<PartialProductResponse> products = new ArrayList<>();

        for (Product product : seller.getProducts()) {
            String Pid = product.getId();
            String PcreatedAt = product.getCreatedAt().toString();
            String title = product.getTitle();
            String description = product.getDescription();
            float suggestedPrice = product.getSuggestedPrice();
            ProductCategory category = product.getCategory();

            Collection<SellerResponseFactory.PartialItemResponse> items = new ArrayList<>();

            for (Offer item : product.getOffers()) {
                items.add(new PartialItemResponse(item.getBuyerUsername(), item.getCreatedAt().toString(), item.getAmount(), item.getMessage()));
            }

            SellerResponseFactory.PartialOfferResponse offers = new PartialOfferResponse(product.getOfferCount(), product.getAvgOfferAmount(), product.getMinOfferAmount(), product.getMaxOfferAmount(), items);
            products.add(new PartialProductResponse(Pid, PcreatedAt, title, description, suggestedPrice, category, offers));
        }
        return products;
    }

    public record SellerResponse(String id,
                                 String createdAt,
                                 String name,
                                 String birthdate,
                                 String email,
                                 String phoneNumber,
                                 String bio,
                                 Collection<SellerResponseFactory.PartialProductResponse> products){}

    public record PartialProductResponse(String id,
                                         String createdAt,
                                         String title,
                                         String description,
                                         float suggestedPrice,
                                         ProductCategory category,
                                         SellerResponseFactory.PartialOfferResponse offers){}

    public record PartialOfferResponse(int count,
                                       @JsonbNillable Float avgAmount,
                                       @JsonbNillable Float minAmount,
                                       @JsonbNillable Float maxAmount,
                                       Collection<SellerResponseFactory.PartialItemResponse> items) {}

    public record PartialItemResponse(String username,
                                      String createdAt,
                                      float amount,
                                      String message) {
    }
}
