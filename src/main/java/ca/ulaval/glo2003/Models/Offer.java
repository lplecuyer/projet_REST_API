package ca.ulaval.glo2003.Models;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Offer {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    private final int offerId = NEXT_ID.getAndIncrement();

    private String productId;

    private String buyerUsername;

    @NotNull(message = "MISSING_PARAMETER: amount is mandatory")
    @DecimalMin(value = "1", message = "INVALID_PARAMETER: amount must be higher than 1$")
    private Float amount;

    @NotBlank(message = "MISSING_PARAMETER: message is mandatory")
    @Size(min = 50, message = "INVALID_PARAMETER: message must be at least 50 characters")
    private String message;

    private final LocalDateTime createdAt = LocalDateTime.now();

    public Offer() {}

    public Offer(float amount, String message) {
        this.amount = amount;
        this.message = message;
    }

    public int getOfferId() {
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

    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
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
