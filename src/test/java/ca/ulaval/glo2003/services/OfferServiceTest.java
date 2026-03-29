package ca.ulaval.glo2003.services;

import ca.ulaval.glo2003.assemblers.OfferAssembler;
import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.ProductRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class OfferServiceTest {

  private static final float VALID_AMOUNT = 78.0f;
  private static final float INVALID_AMOUNT = 0.0f;
  private static final String VALID_MESSAGE = "This is a very long message that contains more than fifty characters.";
  private static final String INVALID_MESSAGE = null;
  private static final String EXISTiNG_PRODUCT_ID = "123";
  private static final String INEXISTiNG_PRODUCT_ID = "123";
  private static final String VALID_BUYERS_ID = "Bob";
  private static final OfferAssembler.OfferRequest invalidOfferRequest = new OfferAssembler.OfferRequest(INVALID_AMOUNT, INVALID_MESSAGE);
  private static final OfferAssembler.OfferRequest validOfferRequest = new OfferAssembler.OfferRequest(VALID_AMOUNT, VALID_MESSAGE);
  private static Product validProduct =  new Product();

  private static OfferService offerService;
  private static ProductRepository productRepository;
  private static ProductAssembler.ProductResponse productResponse;

  @BeforeEach
  void setup(){
    productRepository = mock(ProductRepository.class);
    validProduct = mock(Product.class);
    offerService = new OfferService();
    offerService.setProductRepository(productRepository);
  }

  @Test
  void givenInvalidOfferRequest_whenAddOffer_ThenRequestTossed() {
    when(productRepository.get(any())).thenReturn(validProduct);
    assertThrows(ConstraintViolationException.class,
        () -> offerService.addOffer(invalidOfferRequest, EXISTiNG_PRODUCT_ID, VALID_BUYERS_ID));

    verify(validProduct, never()).addOffer(any());
  }

  @Test
  void givenValidOfferRequest_whenAddOffer_ThenOfferAdded() {
    when(productRepository.get(any())).thenReturn(validProduct);

    offerService.addOffer(validOfferRequest, EXISTiNG_PRODUCT_ID, VALID_BUYERS_ID);

    verify(validProduct, times(1)).addOffer(any());
  }

  @Test
  void givenInexistantProductId_whengetProduct_ThenReturnNull() {
    when(productRepository.get(INEXISTiNG_PRODUCT_ID)).thenReturn(null);
    ProductAssembler.ProductResponse productResponse =
        offerService.getProduct(INEXISTiNG_PRODUCT_ID);

    Assertions.assertNull(productResponse);
  }

  @Test
  void givenExistantPrductId_whengetProduct_ThenReturnProduct() {
    Product product = new Product();
    product.setSeller(new Seller());
    when(productRepository.get(EXISTiNG_PRODUCT_ID)).thenReturn(product);
    ProductAssembler.ProductResponse productResponse =
        offerService.getProduct(EXISTiNG_PRODUCT_ID);

    Assertions.assertNotNull(productResponse);
  }

}