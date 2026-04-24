package ca.ulaval.glo2003.models;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Offer {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    @Id
    private final String offerId = String.valueOf(NEXT_ID.getAndIncrement());

    private String productId;

    @NotNull(message = "MISSING_PARAMETER: buyer's name is mandatory")
    private String buyerUsername;

    @NotNull(message = "MISSING_PARAMETER: amount is mandatory")
    @DecimalMin(value = "1", message = "INVALID_PARAMETER: amount must be higher than 1$")
    private Float amount = 0F;

    @NotNull(message = "MISSING_PARAMETER: message is mandatory")
    @Size(min = 50, message = "INVALID_PARAMETER: message must be at least 50 characters")
    private String message;

    private final LocalDateTime createdAt = LocalDateTime.now();

    public void validate(){
      Set<ConstraintViolation<Offer>> checkResult = validator.validate(this);
      if(!checkResult.isEmpty()) throw new ConstraintViolationException(checkResult);
    }

    public Offer() {}

    public Offer(Float amount, String message, String buyerUsername) {
        this.amount = amount;
        this.message = message;
        this.buyerUsername = buyerUsername;
        validate();
    }

    public String getOfferId() {
        return offerId;
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }
    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }

    public float getAmount() {return amount;}
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
