package ca.ulaval.glo2003.Models;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Product{
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final String id = String.valueOf(NEXT_ID.getAndIncrement());

    @NotNull(message = "MISSING_PARAMETER: title is mandatory")
    @Size(min = 1, max = 50)
    private String title = null;

    @NotNull(message = "MISSING_PARAMETER: description is mandatory")
    @NotEmpty(message = "INVALID_PARAMETER: description cannot be empty")
    private String description = null;

    @NotNull(message = "MISSING_PARAMETER: Category is mandatory")
    private ProductCategory category;

    @DecimalMin(value = "1", message = "INVALID_PARAMETER: suggested price must be higher than 1$")
    private float suggestedPrice = 0;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private Seller seller = null;

    private final ArrayList<Offer> offers = new ArrayList<>();

    public Product(){}

    public Product(String title, String description, ProductCategory category, float suggestedPrice){
        this.title = title;
        this.description = description;
        this.category = category;
        this.suggestedPrice = suggestedPrice;
    }

    public void addOffer(Offer offer){
        offers.add(offer);
    }

    public int getOfferCount(){
        return offers.size();
    }

    public Float getAvgOfferAmount(){
        if (offers.isEmpty()) return null;
        float total = 0.0f;
        for (Offer offer : offers){
            total += offer.getAmount();
        }
        return total/offers.size();
    }

    public String getId() {return id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public ProductCategory getCategory() {return category;}
    public void setCategory(ProductCategory category) {this.category = category;}

    public float getSuggestedPrice() {return suggestedPrice;}
    public void setSuggestedPrice(float suggestedPrice) {this.suggestedPrice = suggestedPrice;}

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public LocalDateTime getCreatedAt() {return createdAt;}

    public Seller getSeller() {return seller;}

    public String getSellerId(){return seller.getId();}

    public void setSeller(Seller seller){this.seller = seller;}

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
}