package ca.ulaval.glo2003.models;

import dev.morphia.annotations.Entity;
import jakarta.validation.constraints.*;

@Entity
public class Review {
  @NotNull(message = "MISSING_PARAMETER: rating comment is mandatory")
  @DecimalMin(value = "0.0", message = "INVALID_PARAMETER: rating must be between 0 and 5")
  @DecimalMax(value = "5.0", message = "INVALID_PARAMETER: rating must be between 0 and 5")
  private float rating;

  @NotNull(message = "MISSING_PARAMETER: comment is mandatory")
  @NotEmpty(message = "INVALID_PARAMETER: comment must not be blank")
  private String comment;

  @NotNull(message = "MISSING_PARAMETER: buyer username is mandatory")
  @NotEmpty(message = "INVALID_PARAMETER: buyer username must not be blank")
  private String buyerUsername;

  private String productId;

  public Review(float rating, String comment, String buyerUsername, String productId) {
    this.rating = rating;
    this.comment = comment;
    this.buyerUsername = buyerUsername;
    this.productId = productId;
  }

  public Review() {}

  public float getRating() { return rating; }
  public String getComment() { return comment; }
  public String getBuyerUsername() { return buyerUsername; }
}
