package ca.ulaval.glo2003.models;

import jakarta.validation.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OfferTest {

    private static final float VALID_AMOUNT = 100.00f;
    private static final float INVALID_TOO_LOW_AMOUNT = 0.5f;
    private static final Float INVALID_NULL_AMOUNT = null;
    private static final String VALID_MESSAGE = "This is a very long message that contains more than fifty characters.";
    private static final String INVALID_SHORT_MESSAGE = "Too short";
    private static final String INVALID_EMPTY_MESSAGE = "";
    private static final String INVALID_NULL_MESSAGE = null;
    private static final String INVALID_BUYERS_NAME = null;
    private static final String VALID_BUYERS_NAME = "Bob";

    private static final String MISSING_PARAMETER = "MISSING_PARAMETER:";
    private static final String INVALID_PARAMETER = "INVALID_PARAMETER:";

    private static Offer offer;
    private static Validator validator;

    @BeforeAll
    static void initialize() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setup(){
      offer = new Offer(VALID_AMOUNT, VALID_MESSAGE, VALID_BUYERS_NAME);
    }

    @Test
    void givenNullAmount_whenValidate_thenMissingParameter() {
        offer.setAmount(INVALID_NULL_AMOUNT);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        Assertions.assertTrue(violations.iterator().next().getMessage().contains(MISSING_PARAMETER));
    }

    @Test
    void givenAmountLowerThanOne_whenValidate_thenInvalidParameter() {
        offer.setAmount(INVALID_TOO_LOW_AMOUNT);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        Assertions.assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
    }

    @Test
    void givenMessageTooShort_whenValidate_thenInvalidParameter() {
        offer.setMessage(INVALID_SHORT_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        Assertions.assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
    }

    @Test
    void givenMessageEmpty_whenValidate_thenInvalidParameter() {
        offer.setMessage(INVALID_EMPTY_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        Assertions.assertTrue(violations.iterator().next().getMessage().contains(INVALID_PARAMETER));
    }

    @Test
    void givenMessageNull_whenValidate_thenMissingParameter() {
        offer.setMessage(INVALID_NULL_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        Assertions.assertTrue(violations.iterator().next().getMessage().contains(MISSING_PARAMETER));
    }

    @Test
    void givenNullBuyersName_whenValidate_thenMissingParameter() {
      offer.setBuyerUsername(INVALID_BUYERS_NAME);

      Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

      Assertions.assertTrue(violations.iterator().next().getMessage().contains(MISSING_PARAMETER));
    }
}
