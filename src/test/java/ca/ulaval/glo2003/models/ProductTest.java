package ca.ulaval.glo2003.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {
    private static final float PRICE_1 = 40.1f;
    private static final float PRICE_2 = 98.9f;
    private static final float PRICE_3 = 20.4f;
    private static final String MESSAGE = "J'aimerais acheter ce téléphone";
    private static final String PRODUCT_TITLE1 = "Apple";
    private static final String PRODUCT_TITLE2 = "Samsung";
    private static final String PRODUCT_DESC1 = "Iphone 13";
    private static final String PRODUCT_DESC2 = "Samsung Galaxy";
    private static final ProductCategory PRODUCT_CATEGORY = ProductCategory.electronics;

    private static final Offer offer1 = new Offer(PRICE_1, MESSAGE);
    private static final Offer offer2 = new Offer(PRICE_2, MESSAGE);
    private static final Offer offer3 = new Offer(PRICE_3, MESSAGE);
    Product productWithNoOffers;
    Product productWithOffers;

    @BeforeEach
    void initialize(){
        productWithNoOffers = new Product(PRODUCT_TITLE1,PRODUCT_DESC1, PRODUCT_CATEGORY, PRICE_3);
        productWithOffers = new Product(PRODUCT_TITLE2,PRODUCT_DESC2, PRODUCT_CATEGORY, PRICE_1);
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
    void givenProductWith3Offers_whenGetAvgOfferAmount_ReturnsAverage(){
        float expectedAverage = (PRICE_1 + PRICE_2 + PRICE_3)/3;
        float actualAverage = productWithOffers.getAvgOfferAmount();
        float errorMargin = 0.001f;
        Assertions.assertEquals(expectedAverage, actualAverage, errorMargin);
    }
}