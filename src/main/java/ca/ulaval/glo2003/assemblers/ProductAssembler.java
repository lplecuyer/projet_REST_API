package ca.ulaval.glo2003.assemblers;

import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Seller;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public final class ProductAssembler {

  private ProductAssembler() {
  }

  public static ProductResponse makeProductResponse(@Valid Product product, @Valid Seller seller) {
    String id = product.getId();
    String title = product.getTitle();
    String description = product.getDescription();
    ProductCategory category = product.getCategory();
    Float suggestedPrice = product.getSuggestedPrice();
    LocalDateTime createdAt = product.getCreatedAt();
    PartialSellerResponse sellerResponse = new PartialSellerResponse(product.getSellerId(), seller.getName());
    PartialOfferResponse offerResponse = new PartialOfferResponse(product.getOfferCount(), product.getAvgOfferAmount());
    boolean vendu = product.getVendu();

    SelectedOfferResponse selectedOffer = null;
    if (product.getSelectedOffer() != null) {
      selectedOffer = new SelectedOfferResponse(product.getSelectedOffer().getBuyerUsername(), product.getSelectedOffer().getAmount());
    }

    ReviewResponse review = null;
    if (product.getReview() != null) {
      review = new ReviewResponse(
          product.getReview().getComment(),
          product.getReview().getRating(),
          product.getReview().getBuyerUsername()
      );
    }

    return new ProductResponse(
        id,
        title,
        description,
        category,
        suggestedPrice,
        createdAt,
        sellerResponse,
        offerResponse,
        vendu,
        vendu ? "sold" : "ongoing",
        selectedOffer,
        review
    );
  }

  public record ProductRequest(String title, String description, String category,
                               Float suggestedPrice) {
  }

  public record ProductResponse(String id, String title, String description,
                                ProductCategory category, float suggestedPrice,
                                LocalDateTime createdAt, PartialSellerResponse seller,
                                PartialOfferResponse offers, boolean vendu, String saleStatus,
                                SelectedOfferResponse selectedOffer, ReviewResponse review) {
  }

  public record ProductsResponse(java.util.Collection<ProductAssembler.ProductResponse> products) { }

  public record PartialSellerResponse(String id, String name) { }

  public record PartialOfferResponse(int count, @JsonbNillable Float avgAmount) { }

  @JsonbNillable
  public record SelectedOfferResponse(String username, Float amount) { }

  public record BuyerUsernameResponse(String username) { }

  public record ReviewRequest(
      @NotNull(message = "MISSING_PARAMETER: comment is mandatory")
      @NotEmpty(message = "INVALID_PARAMETER: comment must not be blank")
      String comment,

      @NotNull(message = "MISSING_PARAMETER: rating is mandatory")
      @DecimalMin(value = "0.0", message = "INVALID_PARAMETER: rating must be between 0 and 5")
      @DecimalMax(value = "5.0", message = "INVALID_PARAMETER: rating must be between 0 and 5")
      Float rating
  ) {}

  public record ReviewResponse(String comment, Float rating, String buyerUsername) {}
}
