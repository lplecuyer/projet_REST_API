package ca.ulaval.glo2003.models;

import ca.ulaval.glo2003.repositories.ProductRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Period;
import jakarta.json.bind.annotation.JsonbTransient;

public class Seller {

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    private final String id = String.valueOf(NEXT_ID.getAndIncrement());

    @Size(min = 1, message = "INVALID_PARAMETER: name cannot be empty")
    @NotNull(message = "MISSING_PARAMETER: Name is mandatory")
    private String name;

    @NotNull(message = "MISSING_PARAMETER: Birthdate is mandatory")
    @Past(message = "INVALID_PARAMETER: Date of birth cannot be set in the future")
    private LocalDate birthdate;

    @AssertTrue(message = "INVALID_PARAMETER: Seller must at least 18 years old")
    @JsonbTransient
    public boolean isAdult() {
        if (birthdate == null) return false;
        return Period.between(birthdate, LocalDate.now()).getYears() >= 18;
    }

    private final LocalDateTime createdAt = LocalDateTime.now();

    @Size(min = 1, message = "INVALID_PARAMETER: Email cannot be empty")
    @Email(message = "INVALID_PARAMETER: Invalid email")
    @NotNull(message = "MISSING_PARAMETER: Email address is mandatory")
    private String email;

    @Size(min = 1, message = "INVALID_PARAMETER: Phone number cannot be empty")
    @NotNull(message = "MISSING_PARAMETER: Phone number is mandatory")
    private String phoneNumber;

    @Size(min = 1, message = "INVALID_PARAMETER: Bio cannot be empty")
    @NotNull(message = "MISSING_PARAMETER: Bio is mandatory")
    private String bio;

    private final Map<String, Product> associatedProducts = new ConcurrentHashMap<>();

    public Seller() {}

    public void addProduct(Product product){
        associatedProducts.put(product.getId(), product);
    }

    public Collection<Product> getProducts(){
        return associatedProducts.values();
    }

    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public LocalDate getBirthdate() { return birthdate; }
    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public void registerProduct(@Valid Product product) {
        product.setSeller(this);
        this.addProduct(product);
        ProductRepository.INSTANCE.save(product);
    }
}