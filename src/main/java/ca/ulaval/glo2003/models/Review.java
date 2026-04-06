package ca.ulaval.glo2003.models;

import dev.morphia.annotations.Entity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.*;

import java.util.Set;

@Entity
public class Review {
  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

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

  public void validate(){
    Set<ConstraintViolation<Review>> checkResult = validator.validate(this);
    if(!checkResult.isEmpty()) throw new ConstraintViolationException(checkResult);
  }

  public Review(float rating, String comment, String buyerUsername) {
    this.rating = rating;
    this.comment = comment;
    this.buyerUsername = buyerUsername;
    validate();
  }

  public Review() {}

  public float getRating() { return rating; }
  public String getComment() { return comment; }
  public String getBuyerUsername() { return buyerUsername; }
}
