package ca.ulaval.glo2003.models;

import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDate;

public class Product{
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    private final int id = NEXT_ID.getAndIncrement();

    @NotBlank(message = "MISSING_PARAMETER: title is mandatory")
    private String title;

    @NotBlank(message = "MISSING_PARAMETER: description is mandatory")
    private String description;

    @NotNull(message = "MISSING_PARAMETER: Category is mandatory")
    private ProductCategory category;

    @DecimalMin(value = "1", message = "INVALID_PARAMETER: suggested price must be higher than 1$")
    private float suggestedPrice;

    private final LocalDate createdAt = LocalDate.now();

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

    public int getId() {return id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public ProductCategory getCategory() {return category;}
    public void setCategory(ProductCategory category) {this.category = category;}

    public float getSuggestedPrice() {return suggestedPrice;}
    public void setSuggestedPrice(float suggestedPrice) {this.suggestedPrice = suggestedPrice;}

    public LocalDate getCreatedAt() {return createdAt;}

    public Seller getSeller() {return seller;}

    public void setSeller(Seller seller){this.seller = seller;}
}