package ca.ulaval.glo2003.Models;

import jakarta.validation.constraints.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDate;

public class Product{
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int id = NEXT_ID.getAndIncrement();

    @NotBlank(message = "Le titre ne peut être vide")
    private String title;

    @NotBlank(message = "La description ne peut être vide")
    private String description;

    @NotBlank(message = "Le produit doit être associé à  une catégorie")
    private String category;

    @AssertTrue(message = "Le prix suggéré doit être d'au minimum 1$")
    boolean isPriceValid(){ return 1f <= suggestedPrice;}
    private float suggestedPrice;


    private final LocalDate creationDate = LocalDate.now();

    public Product(String title,  String description, String category, float suggestedPrice){
        this.title = title;
        this.description = description;
        this.category = category;
        this.suggestedPrice = suggestedPrice;
    }

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