package ca.ulaval.glo2003.Models;

import jakarta.validation.constraints.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDate;

public class Product{
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int id = NEXT_ID.getAndIncrement();

    @NotBlank(message = "MISSING_PARAMETER: title is mandatory")
    private String title;

    @NotBlank(message = "MISSING_PARAMETER: description is mandatory")
    private String description;

    @NotBlank(message = "MISSING_PARAMETER: product must be associated with a category")
    private String category;

    @DecimalMin(value = "1", message = "INVALID_PARAMETER: suggested price must be higher than 1$")
    private float suggestedPrice;

    private final LocalDate creationDate = LocalDate.now();

    public Product(){}

    public int getId() {return id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public float getSuggestedPrice() {return suggestedPrice;}
    public void setSuggestedPrice(float suggestedPrice) {this.suggestedPrice = suggestedPrice;}

    public LocalDate getCreationDate() {return creationDate;}
}