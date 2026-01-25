final class Vendeur {
    private final String name;
    private final LocalDate birthdate;
    private final String email;
    private final String phoneNumber;
    private final String bio;


    public Vendeur(String name, String birthdate, String email, String phoneNumber, String bio) {
        this.name = name;
        this.birthdate = LocalDate.parse(birthdate);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
    }

    public String getName() { return name; }
    public LocalDate getBirthdate() { return birthdate; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getBio() { return bio; }

}