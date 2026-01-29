package ca.ulaval.glo2003.Models;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Period;
import jakarta.json.bind.annotation.JsonbTransient;

public class Seller {
    // id unique incremental
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    private final int id = NEXT_ID.getAndIncrement();

    @NotEmpty(message = "MISSING_PARAMETER: Name is mandatory")
    private String name;

    @Past(message = "INVALID_PARAMETER: Date of birth cannot be set in the future")
    private LocalDate birthdate;

    // micro methode qui verifie si la date recue date de plus de 18 ans
    @AssertTrue(message = "INVALID_PARAMETER: Seller must at least 18 years old")
    @JsonbTransient
    public boolean isAdult() {
        if (birthdate == null) return false;
        return Period.between(birthdate, LocalDate.now()).getYears() >= 18;
    }

    // date de creation de l'usager (non modifiable)
    private final LocalDate createdAt = LocalDate.now();

    @Email(message = "INVALID_PARAMETER: Invalid email")
    @NotBlank(message = "MISSING_PARAMETER: Email address is mandatory")
    private String email;

    @NotBlank(message = "MISSING_PARAMETER: Phone number is mandatory")
    private String phoneNumber;

    @NotBlank(message = "MISSING_PARAMETER: Bio is mandatory")
    private String bio;

    public Seller() {}

    // getters ~ setters
    public int getId() { return id; }

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

}