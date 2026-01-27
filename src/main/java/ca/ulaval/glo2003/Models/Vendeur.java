package ca.ulaval.glo2003.Models;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Period;

public class Vendeur {
    // id unique incremental
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    private final int id = NEXT_ID.getAndIncrement();

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    // micro methode qui verifie si la date recue date de plus de 18 ans
    @AssertTrue(message = "Le vendeur doit avoir 18 ans ou plus")
    public boolean isAdult() {
        System.out.println("birthdate = " + birthdate);
        if (birthdate == null) return false;
        System.out.println("birthdate = " + birthdate);
        return Period.between(birthdate, LocalDate.now()).getYears() >= 18;
    }
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate birthdate;

    // date de creation de l'usager (non modifiable)
    private final LocalDate createdAt = LocalDate.now();

    @Email(message = "Email invalide")
    @NotBlank(message = "L'addresse courriel est obligatoire")
    private String email;

    @NotBlank(message = "Le numero de telephone est obligatoire")
    private String phoneNumber;

    @NotBlank(message = "La bio est obligatoire")
    private String bio;

    public Vendeur() {}

    // getters ~ setters

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