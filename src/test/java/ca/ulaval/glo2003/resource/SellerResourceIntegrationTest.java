package ca.ulaval.glo2003.resource;

import ca.ulaval.glo2003.assemblers.SellerAssembler;
import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.exceptionhandlers.ProcessingExceptionMapper;
import ca.ulaval.glo2003.exceptionhandlers.ValidationExceptionMapper;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.resources.SellerResource;
import ca.ulaval.glo2003.services.SellerService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SellerResourceIntegrationTest extends JerseyTest {

  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private static Set<ConstraintViolation<Seller>> missingParameterError;
  private static Set<ConstraintViolation<Seller>> invalidParameterError;

  private String seller1Id;
  private static SellerAssembler.SellerResponse sellerResponse;

  private static final String VALID_NAME = "John Doe";
  private static  final LocalDate VALID_BIRTHDATE = LocalDate.of(1968, 9, 12);
  private static final String VALID_EMAIL = "john.doe123@gmail.com";
  private static final String VALID_PHONENUMBER = "14181234567";
  private static final String VALID_BIO = "A simple man.";

  private static final String INVALID_ID = "invalid_id";

  private static final String MISSING_PARAMETER = "MISSING_PARAMETER";
  private static final String INVALID_PARAMETER = "INVALID_PARAMETER";
  private static final String NOT_FOUND = "NOT_FOUND";

  private SellerService sellerService;

  @Override
  protected URI getBaseUri() {
    return URI.create("http://localhost:0/");
  }

  @Override
  protected Application configure() {
    sellerService = mock(SellerService.class);
    SellerResource sellerResource = new SellerResource(sellerService);
    createInvalidParameterError();
    createMissingParameterError();

    return new ResourceConfig()
        .register(sellerResource)
        .register(org.glassfish.jersey.jsonb.JsonBindingFeature.class)
        .register(ValidationExceptionMapper.class)
        .register(ProcessingExceptionMapper.class);
  }

  private void createInvalidParameterError() {
    Seller seller = new Seller();
    seller.setBirthdate(LocalDate.now());
    seller.setEmail("");
    seller.setBio("");
    seller.setName("");
    seller.setPhoneNumber("");
    invalidParameterError = validator.validate(seller);
  }

  private void createMissingParameterError() {
    Seller seller = new Seller();
    seller.setBirthdate(LocalDate.parse("1980-12-03"));
    seller.setEmail(null);
    seller.setBio(null);
    seller.setName(null);
    seller.setPhoneNumber(null);
    missingParameterError = validator.validate(seller);
  }

  @BeforeEach
  public void startJersey() throws Exception {
    super.setUp();
    reset(sellerService);

    Seller sellerValide = new Seller(VALID_NAME, VALID_BIRTHDATE.toString(), VALID_EMAIL, VALID_PHONENUMBER, VALID_BIO);
    sellerValide.addProduct(new Product("iPhone Case", "desc", ProductCategory.electronics, 15F));
    sellerResponse = SellerAssembler.makeSellerResponse(sellerValide);

    seller1Id = sellerValide.getId();

    when(sellerService.getSeller(seller1Id)).thenReturn(sellerResponse);
  }

  @AfterEach
  public void stopJersey() throws Exception {
    super.tearDown();
  }

  @Test
  public void givenValidSeller_whenPostSeller_thenCreated() {
    Map<String, Object> data = validSellerPayload();
    when(sellerService.addSeller(any())).thenReturn(sellerResponse);

    Response response = postSeller(data);

    assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
  }

  @Test
  public void givenMissingName_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("name");
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(missingParameterError));

    Response response = postSeller(data);

    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(MISSING_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidName_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("name");
    data.put("name", "");
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(invalidParameterError));

    Response response = postSeller(data);

    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(INVALID_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidBirthdate_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("birthdate");
    data.put("birthdate", "");
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(invalidParameterError));

    Response response = postSeller(data);
    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void givenAgeBelow18_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("birthdate");
    data.put("birthdate", LocalDate.now().toString());
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(invalidParameterError));

    Response response = postSeller(data);

    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(INVALID_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenMissingEmail_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("email");
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(missingParameterError));

    Response response = postSeller(data);

    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(MISSING_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidEmail_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("email");
    data.put("email", "");
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(invalidParameterError));

    Response response = postSeller(data);

    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(INVALID_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenMissingPhoneNumber_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("phoneNumber");
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(missingParameterError));

    Response response = postSeller(data);

    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(MISSING_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidPhoneNumber_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("phoneNumber");
    data.put("phoneNumber", "");
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(invalidParameterError));

    Response response = postSeller(data);

    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(INVALID_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenMissingBio_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("bio");
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(missingParameterError));

    Response response = postSeller(data);

    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(MISSING_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidBio_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("bio");
    data.put("bio", "");
    when(sellerService.addSeller(any()))
        .thenThrow( new ConstraintViolationException(invalidParameterError));

    Response response = postSeller(data);

    verify(sellerService, times(1)).addSeller(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(INVALID_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenValidSellerId_whenGetSeller_thenOk() {
    Response response = getSeller(seller1Id);

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
  }

  @Test
  public void givenInvalidSellerId_whenGetSeller_thenNotFound() {
    Response response = getSeller(INVALID_ID);

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertEquals(NOT_FOUND, response.readEntity(ErrorResponse.class).error);
  }

  private Response postSeller(Map<String, Object> data) {
    return target("sellers")
        .request(MediaType.APPLICATION_JSON)
        .post(Entity.json(data));
  }

  private Response getSeller(String sellerId) {
    return target("sellers")
        .path(sellerId)
        .request(MediaType.APPLICATION_JSON)
        .get();
  }

  private Map<String, Object> validSellerPayload() {
    Map<String, Object> data = new HashMap<>();
    data.put("name", VALID_NAME);
    data.put("birthdate", VALID_BIRTHDATE.toString());
    data.put("email", VALID_EMAIL);
    data.put("phoneNumber", VALID_PHONENUMBER);
    data.put("bio", VALID_BIO);
    return data;
  }
}
