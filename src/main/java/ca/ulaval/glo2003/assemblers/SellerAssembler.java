package ca.ulaval.glo2003.assemblers;

import ca.ulaval.glo2003.models.Offer;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Seller;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;

public final class SellerAssembler {

    private SellerAssembler() {}

    public static SellerResponse makeSellerResponse(@Valid Seller seller) {
        String id = seller.getId();
        String createdAt = seller.getCreatedAt().toString();
        String name = seller.getName();
        String birthdate = seller.getBirthdate().toString();
        String email = seller.getEmail();
        String phoneNumber = seller.getPhoneNumber();
        String bio = seller.getBio();
        Collection<SellerAssembler.PartialProductResponse> products = makePartialProductResponse(seller);

        return new SellerResponse(id, createdAt, name, birthdate, email, phoneNumber, bio, products);
    }

    public static Collection<PartialProductResponse> makePartialProductResponse(Seller seller) {
        Collection<PartialProductResponse> products = new ArrayList<>();

        for (Product product : seller.getProducts()) {
            String productId = product.getId();
            String productCreatedAt = product.getCreatedAt().toString();
            String title = product.getTitle();
            String description = product.getDescription();
            float suggestedPrice = product.getSuggestedPrice();
            ProductCategory category = product.getCategory();
            boolean vendu = product.getVendu();

            Collection<SellerAssembler.PartialItemResponse> items = new ArrayList<>();

            for (Offer item : product.getOffers()) {
                items.add(new PartialItemResponse(item.getBuyerUsername(), item.getCreatedAt().toString(), item.getAmount(), item.getMessage()));
            }

            SellerAssembler.PartialOfferResponse offers = new PartialOfferResponse(product.getOfferCount(), product.getAvgOfferAmount(), product.getMinOfferAmount(), product.getMaxOfferAmount(), items);
            products.add(new PartialProductResponse(productId, productCreatedAt, title, description, suggestedPrice, category, offers, vendu ? "sold" : "ongoing"));
        }
        return products;
    }

  public record SellerRequest(String name,
                               String birthdate,
                               String email,
                               String phoneNumber,
                               String bio){}

    public record SellerResponse(String id,
                                 String createdAt,
                                 String name,
                                 String birthdate,
                                 String email,
                                 String phoneNumber,
                                 String bio,
                                 Collection<SellerAssembler.PartialProductResponse> products){}

    public record PartialProductResponse(String id,
                                         String createdAt,
                                         String title,
                                         String description,
                                         float suggestedPrice,
                                         ProductCategory category,
                                         SellerAssembler.PartialOfferResponse offers,
                                         String saleStatus){}

    public record PartialOfferResponse(int count,
                                       @JsonbNillable Float avgAmount,
                                       @JsonbNillable Float minAmount,
                                       @JsonbNillable Float maxAmount,
                                       Collection<SellerAssembler.PartialItemResponse> items) {}

    public record PartialItemResponse(String username,
                                      String createdAt,
                                      float amount,
                                      String message) {
    }
}
