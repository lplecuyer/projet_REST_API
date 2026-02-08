package ca.ulaval.glo2003.Models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {

    Product productWithNoOffers;
    Product productWithOffers;
    Offer offer1;
    Offer offer2;
    Offer offer3;
    private static final float PRICE_1 = 40.1f;
    private static final float PRICE_2 = 98.9f;
    private static final float PRICE_3 = 20.4f;

    @BeforeEach
    void initialize(){
        productWithNoOffers = new Product("Cellphone","Iphone 13", "Slim", 36.8f);
        productWithOffers = new Product("Cellphone","Samsung Galaxy", "Slim", 36.8f);
        offer1 = new Offer(PRICE_1, "non");
        offer2 = new Offer(PRICE_2, "oui");
        offer3 = new Offer(PRICE_3, "oui");
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
        float expectedAaverage = (PRICE_1 + PRICE_2 + PRICE_3)/3;
        float actualAverage = productWithOffers.getAvgOfferAmount();
        float errorMargin = 0.001f;
        Assertions.assertEquals(expectedAaverage, actualAverage, errorMargin);
    }
}