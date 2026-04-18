package ca.ulaval.glo2003.services;

import ca.ulaval.glo2003.assemblers.SellerAssembler;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class SellerServiceTest {

  private static final String INVALID_STRING = null;
  private static final String VALID_SELLER_NAME = "Bob";
  private static final String VALID_SELLER_BIRTHDATE = LocalDate.of(1987,11,11).toString();
  private static final String VALID_SELLER_EMAIL = "Bob@gmail.com";
  private static final String VALID_SELLER_PHONENUMBER = "823 232-4125";
  private static final String VALID_SELLER_BIO = "Im Bob";
  private static final String INEXISTING_SELLER_ID =  "-1";
  private static final String EXISTING_SELLER_ID = "2134";

  private static final SellerAssembler.SellerRequest invalidSellerRequest
      = new SellerAssembler.SellerRequest(INVALID_STRING, VALID_SELLER_BIRTHDATE,INVALID_STRING,INVALID_STRING,INVALID_STRING);
  private static final SellerAssembler.SellerRequest validSellerRequest
      = new SellerAssembler.SellerRequest(VALID_SELLER_NAME,VALID_SELLER_BIRTHDATE,
      VALID_SELLER_EMAIL,VALID_SELLER_PHONENUMBER,VALID_SELLER_BIO);


  private static ISellerRepository sellerRepository;
  private static SellerService sellerService;

  @BeforeEach
  void setup(){
    sellerRepository = mock(ISellerRepository.class);
    sellerService = new SellerService(sellerRepository);
  }

  @Test
  void givenInvalidSellerRequest_whenAddSeller_ThenRequestTossed() {
    assertThrows(ConstraintViolationException.class,
        () -> sellerService.addSeller(invalidSellerRequest));

    verify(sellerRepository, never()).save(any());
  }

  @Test
  void givenValidSellerRequest_whenAddSeller_ThenSellerAdded() {
    sellerService.addSeller(validSellerRequest);

    verify(sellerRepository, times(1)).save(any());
  }

  @Test
  void givenInexistantSellerId_whenGetSeller_ThenReturnNull() {
    when(sellerRepository.get(any())).thenReturn(null);
    SellerAssembler.SellerResponse sellerResponse =
        sellerService.getSeller(INEXISTING_SELLER_ID);

    Assertions.assertNull(sellerResponse);
  }

  @Test
  void givenExistantPrductId_whengetProduct_ThenReturnProduct() {
    Seller seller = new Seller(VALID_SELLER_NAME,VALID_SELLER_BIRTHDATE,VALID_SELLER_EMAIL,VALID_SELLER_PHONENUMBER,VALID_SELLER_BIO);
    when(sellerRepository.get(any())).thenReturn(seller);
    SellerAssembler.SellerResponse sellerResponse =
        sellerService.getSeller(EXISTING_SELLER_ID);

    Assertions.assertNotNull(sellerResponse);
  }
}