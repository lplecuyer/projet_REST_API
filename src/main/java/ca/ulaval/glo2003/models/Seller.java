package ca.ulaval.glo2003.models;

import dev.morphia.annotations.Id;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Period;
import jakarta.json.bind.annotation.JsonbTransient;
import dev.morphia.annotations.Entity;


@Entity
public class Seller {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    @Id
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

    private List<Product> associatedProducts = new ArrayList<>();

    private LocalDate convertDate(String stringDate){
      LocalDate localDate;
      try{
        localDate = LocalDate.parse(stringDate);
      } catch (DateTimeParseException e){
        localDate = LocalDate.now();
      }
      return localDate;
    }

    public void validate(){
      Set<ConstraintViolation<Seller>> checkResult = validator.validate(this);
      if(!checkResult.isEmpty()) throw new ConstraintViolationException(checkResult);
    }

    public Seller(String name, String birthdate, String email, String phoneNumber, String bio) {
      this.name = name;
      this.birthdate = convertDate(birthdate);
      this.email = email;
      this.phoneNumber = phoneNumber;
      this.bio = bio;
      validate();
    }

    public Seller(){}

    public void addProduct(Product product){
        associatedProducts.add(product);
    }

    public Collection<Product> getProducts(){
        return associatedProducts;
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
}