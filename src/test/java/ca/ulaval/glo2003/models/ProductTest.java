package ca.ulaval.glo2003.models;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.*;

import java.util.Set;


class ProductTest {
  private static final String MISSING_PARAMETER = "MISSING_PARAMETER";
  private static final String INVALID_PARAMETER = "INVALID_PARAMETER";
  private static final float VALID_PRICE_1 = 40.1f;
  private static final float VALID_PRICE_2 = 98.9f;
  private static final float VALID_PRICE_3 = 20.4f;
  private static final String VALID_OFFER_MESSAGE = "This is a very long message that contains more than fifty characters.";
  private static final String BUYERS_NAME = "Bob";
  private static final String VALID_TITLE = "Apple";
  private static final String VALID_DESC = "Iphone 13";
  private static final ProductCategory VALID_CATEGORY = ProductCategory.electronics;
  private static final String EMPTY_STRING = "";
  private static final Float INVALID_PRICE = 0F;
  private static final Float VALID_DISCOUNT = 50F;
  private static final Float VALID_DISCOUNT_ZERO = 0F;
  private static final Float INVALID_DISCOUNT_TOO_LOW = -1F;
  private static final Float INVALID_DISCOUNT_TOO_HIGH = 101F;

  private static Validator validator;
  private static final Offer offer1 = new Offer(VALID_PRICE_1, VALID_OFFER_MESSAGE, BUYERS_NAME);
  private static final Offer offer2 = new Offer(VALID_PRICE_2, VALID_OFFER_MESSAGE, BUYERS_NAME);
  private static final Offer offer3 = new Offer(VALID_PRICE_3, VALID_OFFER_MESSAGE, BUYERS_NAME);
  private static Product validProduct;
  private static Product product;
  private static Product productWithNoOffers;
  private static Product productWithOffers;

  @BeforeAll
  static void initialize(){
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator =  factory.getValidator();
  }

  @BeforeEach
  void setup(){
    validProduct = new Product(VALID_TITLE, VALID_DESC, VALID_CATEGORY, VALID_PRICE_1);
    product = new Product(VALID_TITLE, VALID_DESC, VALID_CATEGORY, VALID_PRICE_1);
    productWithNoOffers = new Product(VALID_TITLE, VALID_DESC, VALID_CATEGORY, VALID_PRICE_1);
    productWithOffers = new Product(VALID_TITLE, VALID_DESC, VALID_CATEGORY, VALID_PRICE_1);
    productWithOffers.addOffer(offer1);
    productWithOffers.addOffer(offer2);
    productWithOffers.addOffer(offer3);
  }

  @Test
  void givenProductWithNoOffers_whenGetAvgOfferAmount_ReturnsNull() {
    Assertions.assertEquals(0, productWithNoOffers.getOfferCount());
    Assertions.assertNull(productWithNoOffers.getAvgOfferAmount());
  }

  @Test
  void givenValidParameters_whenValide_thenNoViolation(){
    Set<ConstraintViolation<Product>> violations = validator.validate(validProduct);
    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  void givenNullTitle_whenValidate_createsViolation() {
    product.setTitle(null);
    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    String actualMessage = violations.iterator().next().getMessage();
    Assertions.assertTrue(actualMessage.contains(MISSING_PARAMETER));
  }

  @Test
  void givenEmptyTitle_whenValidate_createsViolation() {
    product.setTitle(EMPTY_STRING);
    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    String actualMessage = violations.iterator().next().getMessage();
    Assertions.assertTrue(actualMessage.contains(INVALID_PARAMETER));
  }

  @Test
  void givenNullDescription_whenValidate_createsViolation() {
    product.setDescription(null);
    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    String actualMessage = violations.iterator().next().getMessage();
    Assertions.assertTrue(actualMessage.contains(MISSING_PARAMETER));
  }

  @Test
  void givenEmptyDescription_whenValidate_createsViolation() {
    product.setDescription(EMPTY_STRING);
    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    String actualMessage = violations.iterator().next().getMessage();
    Assertions.assertTrue(actualMessage.contains(INVALID_PARAMETER));
  }

  @Test
  void givenNullCategory_whenValidate_createsViolation() {
    product.setCategory(null);
    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    String actualMessage = violations.iterator().next().getMessage();
    Assertions.assertTrue(actualMessage.contains(MISSING_PARAMETER));
  }

  @Test
  void givenNullSuggestedPrice_whenValidate_createsViolation() {
    product.setSuggestedPrice(null);
    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    String actualMessage = violations.iterator().next().getMessage();
    Assertions.assertTrue(actualMessage.contains(MISSING_PARAMETER));
  }

  @Test
  void givenInvalidPrice_whenValidate_createsViolation() {
    product.setSuggestedPrice(INVALID_PRICE);
    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    String actualMessage = violations.iterator().next().getMessage();
    Assertions.assertTrue(actualMessage.contains(INVALID_PARAMETER));
  }

  @Test
  void givenProductWith3offers_whenGetAvgOfferAmount_ReturnsAverage(){
    float expectedAverage = (VALID_PRICE_1 + VALID_PRICE_2 + VALID_PRICE_3)/3;
    float actualAverage = productWithOffers.getAvgOfferAmount();
    float errorMargin = 0.001f;
    Assertions.assertEquals(expectedAverage, actualAverage, errorMargin);
  }

  @Test
  void givenValidDiscount_whenValidate_thenNoViolation() {
    product.setDiscount(VALID_DISCOUNT);

    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  void givenNegativeDiscount_whenValidate_thenViolation() {
    product.setDiscount(INVALID_DISCOUNT_TOO_LOW);

    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    String actualMessage = violations.iterator().next().getMessage();
    Assertions.assertTrue(actualMessage.contains(INVALID_PARAMETER));
  }

  @Test
  void givenDiscountGreaterThan100_whenValidate_thenViolation() {
    product.setDiscount(INVALID_DISCOUNT_TOO_HIGH);

    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    String actualMessage = violations.iterator().next().getMessage();
    Assertions.assertTrue(actualMessage.contains(INVALID_PARAMETER));
  }

  @Test
  void givenDiscountApplied_whenGetPrice_thenCorrectValue() {
    product.setSuggestedPrice(VALID_PRICE_1);
    product.setDiscount(VALID_DISCOUNT);

    float expected = VALID_PRICE_1/2;
    float actual = product.getSuggestedPrice();

    Assertions.assertEquals(expected, actual, 0.001f);
  }

  @Test
  void givenDiscountApplied_whenGetPriceAfterDiscount_thenCorrectValue() {
    product.setSuggestedPrice(VALID_PRICE_1);
    product.setDiscount(VALID_DISCOUNT);

    float expected = VALID_PRICE_1/2;
    float actual = product.getSuggestedPrice();

    Assertions.assertEquals(expected, actual, 0.001f);
  }

  @Test
  void givenDefaultDiscount_whenGetPriceAfterDiscount_thenSamePrice() {
    product.setSuggestedPrice(VALID_PRICE_1);

    float actual = product.getSuggestedPrice();

    Assertions.assertEquals(VALID_PRICE_1, actual);
  }

  @Test
  void givenZeroDiscount_whenGetPriceAfterDiscount_thenSamePrice() {
    product.setSuggestedPrice(VALID_PRICE_1);
    product.setDiscount(VALID_DISCOUNT_ZERO);

    float actual = product.getSuggestedPrice();

    Assertions.assertEquals(VALID_PRICE_1, actual);
  }
}