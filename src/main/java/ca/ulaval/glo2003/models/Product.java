package ca.ulaval.glo2003.models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import jakarta.validation.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Product {
  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

  @Id
  private final String id = "A" + NEXT_ID.getAndIncrement();

  @NotNull(message = "MISSING_PARAMETER: title is mandatory")
  @Size(min = 1, message = "INVALID_PARAMETER: title must not be blank")
  private String title = null;

  @NotNull(message = "MISSING_PARAMETER: description is mandatory")
  @Size(min = 1, message = "INVALID_PARAMETER: description must not be blank")
  private String description = null;

  @NotNull(message = "MISSING_PARAMETER: Category is mandatory")
  private ProductCategory category;

  @NotNull(message = "MISSING_PARAMETER: suggested price is mandatory")
  @DecimalMin(value = "1", message = "INVALID_PARAMETER: suggested price must be higher than 1$")
  private Float suggestedPrice = 1F;

  @Min(value = 0, message = "INVALID_PARAMETER: discount must be >= 0")
  @Max(value = 100, message = "INVALID_PARAMETER: discount must be <= 100")
  private Float discount = 0F;

  private final LocalDateTime createdAt = LocalDateTime.now();

  private String sellerId;

  private final List<Offer> offers = new ArrayList<>();

  private boolean sold;

  private Offer selectedOffer = null;

  @Valid
  private Review review;

  public void validate() {
    Set<ConstraintViolation<Product>> checkResult = validator.validate(this);
    if (!checkResult.isEmpty()) throw new ConstraintViolationException(checkResult);
  }

  public Product() {
  }

  public Product(String title, String description, ProductCategory category, Float suggestedPrice) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.suggestedPrice = suggestedPrice;
    this.sold = false;
    validate();
  }

  public void addOffer(Offer offer) {
    offer.setProductId(this.id);
    offers.add(offer);
  }

  public int getOfferCount() {
    return offers.size();
  }

  public Float getAvgOfferAmount() {
    if (offers.isEmpty()) return null;
    float total = 0.0f;
    for (Offer offer : offers) {
      total += offer.getAmount();
    }
    return total / offers.size();
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductCategory getCategory() {
    return category;
  }

  public void setCategory(ProductCategory category) {
    this.category = category;
  }

  public Float getSuggestedPrice() {
    return suggestedPrice;
  }

  public void setSuggestedPrice(Float suggestedPrice) {
    this.suggestedPrice = suggestedPrice;
  }

  public List<Offer> getOffers() {
    return offers;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getSellerId() {
    return sellerId;
  }

  public void setSellerId(String sellerId) {
    this.sellerId = sellerId;
  }

  public boolean getSold() {
    return sold;
  }

  public void setSold(boolean sold) {
    this.sold = sold;
  }

  public Review getReview() {return this.review;}

  public void setReview(@Valid Review review) { this.review = review; }

  public Float getMinOfferAmount() {
    float min = Float.MAX_VALUE;
    for (Offer offer : offers) {
      if (min > offer.getAmount()) min = offer.getAmount();
    }
    return min == Float.MAX_VALUE ? null : min;
  }

  public Float getMaxOfferAmount() {
    float max = Float.MIN_VALUE;
    for (Offer offer : offers) {
      if (max < offer.getAmount()) max = offer.getAmount();
    }
    return max == Float.MIN_VALUE ? null : max;
  }

  public boolean offerForSameProductAndUserAlreadyExists(String buyerUsername) {
    boolean alreadyExists = false;
    for (Offer offer : this.getOffers()) {
      alreadyExists = offer.getBuyerUsername().equals(buyerUsername);
    }
    return alreadyExists;
  }

  public Offer getSelectedOffer() {
    if (this.sold) {
      return selectedOffer;
    } else return null;
  }

  public void setSelectedOffer(Offer selectedOffer) {
    this.selectedOffer = selectedOffer;
  }

  public void setDiscount(Float discount) {
    this.suggestedPrice = revertDiscount();
    this.discount = discount;
    this.suggestedPrice = getPriceAfterDiscount();
  }

  public Float getDiscount() {
    return discount;
  }

  public float revertDiscount() {
    return suggestedPrice / (1 - discount / 100);
  }

  public float getPriceAfterDiscount() {
    return suggestedPrice * (1 - discount / 100);
  }
}