package ca.ulaval.glo2003.Models;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SellerTest {

  Seller seller;
  private static final String VALID_NAME = "John Doe";
  private static  final LocalDate VALID_BIRTHDATE = LocalDate.of(1968, 9, 12);
  private static final String VALID_EMAIL = "john.doe123@gmail.com";
  private static final String VALID_PHONENUMBER = "14181234567";
  private static final String VALID_BIO = "A simple man.";

  Product product =  new Product();

  private static final String MISSING_PARAMETER = "MISSING_PARAMETER:";
  private static final String INVALID_PARAMETER = "INVALID_PARAMETER:";

  private static Validator validator;

  @BeforeAll
  static void initialize() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @BeforeEach
  void setUp() {
    seller = new Seller();
    seller.setName(VALID_NAME);
    seller.setBirthdate(VALID_BIRTHDATE);
    seller.setEmail(VALID_EMAIL);
    seller.setPhoneNumber(VALID_PHONENUMBER);
    seller.setBio(VALID_BIO);
  }

  @Test
  void givenSellerNameNull_whenValidate_thenMissingParameter() {
    seller.setName(null);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(MISSING_PARAMETER));
  }

  @Test
  void givenSellerNameEmpty_whenValidate_thenInvalidParameter() {
    seller.setName("");

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
  }

  @Test
  void givenSellerNameValid_whenValidate_thenValid() {
    seller.setName(VALID_NAME);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenSellerBirthDateNull_whenValidate_thenMissingParameter() {
    seller.setBirthdate(null);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    for (ConstraintViolation<Seller> violation : violations) {
      assertTrue(violation.getMessage().contains(MISSING_PARAMETER)
      || violation.getMessage().contains(INVALID_PARAMETER));
    }
  }

  @Test
  void givenSellerBirthDateFuture_whenValidate_thenInvalidParameter() {
    seller.setBirthdate(LocalDate.now().plusYears(1));

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
  }

  @Test
  void givenSellerBirthDateValid_whenValidate_thenValid() {
    seller.setBirthdate(VALID_BIRTHDATE);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenSellerNotAdult_whenIsAdult_thenInvalidParameter() {
    seller.setBirthdate(LocalDate.now());

    seller.isAdult();
    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
  }

  @Test
  void givenSellerAdult_whenIsAdult_thenValid() {
    seller.setBirthdate(VALID_BIRTHDATE);

    seller.isAdult();
    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenSellerInvalidEmail_whenValidate_thenInvalidParameter() {
    seller.setEmail(VALID_PHONENUMBER);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
  }

  @Test
  void givenSellerEmptyEmail_whenValidate_thenInvalidParameter() {
    seller.setEmail("");

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
  }

  @Test
  void givenSellerEmailNull_whenValidate_thenMissingParameter() {
    seller.setEmail(null);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(MISSING_PARAMETER));
  }

  @Test
  void givenSellerValidEmail_whenValidate_thenValid() {
    seller.setEmail(VALID_EMAIL);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenSellerEmptyPhoneNumber_whenValidate_thenInvalidParameter() {
    seller.setPhoneNumber("");

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
  }

  @Test
  void givenSellerPhoneNumberNull_whenValidate_thenMissingParameter() {
    seller.setPhoneNumber(null);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(MISSING_PARAMETER));
  }

  @Test
  void givenSellerValidPhoneNumber_whenValidate_thenValid() {
    seller.setPhoneNumber(VALID_PHONENUMBER);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenSellerBioEmpty_whenValidate_thenInvalidParameter() {
    seller.setBio("");

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
  }

  @Test
  void givenSellerBioNull_whenValidate_thenMissingParameter() {
    seller.setBio(null);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.iterator().next().getMessage().contains(MISSING_PARAMETER));
  }

  @Test
  void givenSellerValidBio_whenValidate_thenValid() {
    seller.setBio(VALID_BIO);

    Set<ConstraintViolation<Seller>> violations = validator.validate(seller);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenSellerNoProduct_whenAddProduct_thenAddProductWithId1() {
    seller.addProduct(product);

    assertEquals(1, seller.getProducts().size());
    assertEquals(product, seller.getProducts().iterator().next());
  }

  @Test
  void givenInvalidProduct_whenRegisterProduct_NoInsertion() {
    product.setDescription(null);

    Set<ConstraintViolation<Product>> violations = validator.validate(product);
    assertFalse(violations.isEmpty());

    seller.registerProduct(product);
  }
}