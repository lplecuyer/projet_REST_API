package ca.ulaval.glo2003.assemblers;

import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Seller;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

public final class ProductAssembler {

  private ProductAssembler() {
  }

  public static ProductResponse makeProductResponse(@Valid Product product, @Valid Seller seller) {
    String id = product.getId();
    String title = product.getTitle();
    String description = product.getDescription();
    ProductCategory category = product.getCategory();
    float suggestedPrice = product.getSuggestedPrice();
    LocalDateTime createdAt = product.getCreatedAt();
    PartialSellerResponse sellerResponse = new PartialSellerResponse(product.getSellerId(), seller.getName());
    PartialOfferResponse offerResponse = new PartialOfferResponse(product.getOfferCount(), product.getAvgOfferAmount());
    boolean vendu = product.getVendu();
    SelectedOfferResponse selectedOffer = null;
    if (product.getSelectedOffer() != null) {
      selectedOffer = new SelectedOfferResponse(product.getSelectedOffer().getBuyerUsername(), product.getSelectedOffer().getAmount());
    }
    return new ProductResponse(id, title, description, category, suggestedPrice, createdAt, sellerResponse, offerResponse, vendu, vendu ? "sold" : "ongoing", selectedOffer);
  }

  public record ProductRequest(String title, String description, String category,
                               float suggestedPrice) {
  }

  public record ProductResponse(String id, String title, String description,
                                ProductCategory category, float suggestedPrice,
                                LocalDateTime createdAt, PartialSellerResponse seller,
                                PartialOfferResponse offers, boolean vendu, String saleStatus,
                                SelectedOfferResponse selectedOffer) {
  }

  public record ProductsResponse(java.util.Collection<ProductAssembler.ProductResponse> products) {
  }

  public record PartialSellerResponse(String id, String name) {
  }

  public record PartialOfferResponse(int count, @JsonbNillable Float avgAmount) {
  }

  @JsonbNillable
  public record SelectedOfferResponse(String username, Float amount) {
  }

  public record BuyerUsernameResponse(String username) {
  }
}
