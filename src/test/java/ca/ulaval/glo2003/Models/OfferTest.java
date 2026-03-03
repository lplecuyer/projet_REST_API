package ca.ulaval.glo2003.Models;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class OfferTest {

    private static final float VALID_AMOUNT = 100.00f;
    private static final float INVALID_TOO_LOW_AMOUNT = 0.5f;
    private static final Float INVALID_NULL_AMOUNT = null;
    private static final String VALID_MESSAGE = "This is a very long message that contains more than fifty characters.";
    private static final String INVALID_SHORT_MESSAGE = "Too short";
    private static final String INVALID_EMPTY_MESSAGE = "";
    private static final String INVALID_NULL_MESSAGE = null;
    private static Validator validator;

    @BeforeAll
    static void initialize() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void givenValidParameters_whenValidate_thenNoViolation() {
        Offer offer = new Offer(VALID_AMOUNT, VALID_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullAmount_whenValidate_thenViolation() {
        Offer offer = new Offer();
        offer.setMessage(VALID_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        assertFalse(violations.isEmpty());
    }

    @Test
    void givenAmountLowerThanOne_whenValidate_thenViolation() {
        Offer offer = new Offer(INVALID_TOO_LOW_AMOUNT, VALID_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        assertFalse(violations.isEmpty());
    }

    @Test
    void givenAmountNull_whenValidate_thenViolation() {
        Offer offer = new Offer(INVALID_NULL_AMOUNT, VALID_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        assertFalse(violations.isEmpty());
    }

    @Test
    void givenMessageTooShort_whenValidate_thenViolation() {
        Offer offer = new Offer(VALID_AMOUNT, INVALID_SHORT_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        assertFalse(violations.isEmpty());
    }

    @Test
    void givenMessageEmpty_whenValidate_thenViolation() {
        Offer offer = new Offer(VALID_AMOUNT, INVALID_EMPTY_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        assertFalse(violations.isEmpty());
    }

    @Test
    void givenMessageNull_whenValidate_thenViolation() {
        Offer offer = new Offer(VALID_AMOUNT, INVALID_NULL_MESSAGE);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        assertFalse(violations.isEmpty());
    }
}
