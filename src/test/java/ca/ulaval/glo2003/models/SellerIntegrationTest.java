package ca.ulaval.glo2003.models;

import ca.ulaval.glo2003.ServerApplication;
import ca.ulaval.glo2003.exceptionHandlers.ErrorResponse;
import ca.ulaval.glo2003.exceptionHandlers.ProcessingExceptionMapper;
import ca.ulaval.glo2003.exceptionHandlers.ValidationExceptionMapper;
import ca.ulaval.glo2003.repositories.SellerRepository;
import ca.ulaval.glo2003.resources.SellerResource;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SellerIntegrationTest extends JerseyTest {

  private String seller1Id;
  private static String seller2Id;

  private static final String VALID_NAME = "John Doe";
  private static  final LocalDate VALID_BIRTHDATE = LocalDate.of(1968, 9, 12);
  private static final String VALID_EMAIL = "john.doe123@gmail.com";
  private static final String VALID_PHONENUMBER = "14181234567";
  private static final String VALID_BIO = "A simple man.";

  private static final String INVALID_ID = "invalid_id";

  private static final String MISSING_PARAMETER = "MISSING_PARAMETER";
  private static final String INVALID_PARAMETER = "INVALID_PARAMETER";
  private static final String NOT_FOUND = "NOT_FOUND";

  private SellerRepository sellerRepo;

  @Override
  protected URI getBaseUri() {
    return URI.create("http://localhost:0/");
  }

  @Override
  protected Application configure() {
    sellerRepo = mock(SellerRepository.class);
    ServerApplication serverApplication = new ServerApplication();

    return new ResourceConfig()
        .register(new SellerResource(serverApplication))
        .register(org.glassfish.jersey.jsonb.JsonBindingFeature.class)
        .register(ValidationExceptionMapper.class)
        .register(ProcessingExceptionMapper.class);
  }

  @BeforeEach
  public void startJersey() throws Exception {
    super.setUp();
    reset(sellerRepo);

    Seller sellerValide = new Seller(VALID_NAME, VALID_BIRTHDATE.toString(), VALID_EMAIL,VALID_PHONENUMBER, VALID_BIO);
    sellerValide.addProduct(new Product("iPhone Case", "desc", ProductCategory.electronics, 15F));

    seller1Id = sellerValide.getId();

    when(sellerRepo.get(seller1Id)).thenReturn(sellerValide);
  }

  @AfterEach
  public void stopJersey() throws Exception {
    super.tearDown();
  }

  @Test
  public void givenValidSeller_whenPostSeller_thenCreated() {
    Map<String, Object> data = validSellerPayload();

    Response response = postSeller(data);

    assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
  }

  @Test
  public void givenMissingName_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("name");

    Response response = postSeller(data);

    verify(sellerRepo, never()).save(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(MISSING_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidName_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("name");
    data.put("name", "");

    Response response = postSeller(data);

    verify(sellerRepo, never()).save(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(INVALID_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidBirthdate_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("birthdate");
    data.put("birthdate", "");

    Response response = postSeller(data);
    verify(sellerRepo, never()).save(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void givenAgeBelow18_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("birthdate");
    data.put("birthdate", LocalDate.now().toString());

    Response response = postSeller(data);

    verify(sellerRepo, never()).save(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(INVALID_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenMissingEmail_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("email");

    Response response = postSeller(data);

    verify(sellerRepo, never()).save(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(MISSING_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidEmail_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("email");
    data.put("email", "");

    Response response = postSeller(data);

    verify(sellerRepo, never()).save(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(INVALID_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenMissingPhoneNumber_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("phoneNumber");

    Response response = postSeller(data);

    verify(sellerRepo, never()).save(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(MISSING_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidPhoneNumber_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("phoneNumber");
    data.put("phoneNumber", "");

    Response response = postSeller(data);

    verify(sellerRepo, never()).save(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(INVALID_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenMissingBio_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("bio");

    Response response = postSeller(data);

    verify(sellerRepo, never()).save(any());
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertEquals(MISSING_PARAMETER, response.readEntity(ErrorResponse.class).error);
  }

  @Test
  public void givenInvalidBio_whenPostSeller_thenNotFound() {
    Map<String, Object> data = validSellerPayload();
    data.remove("bio");
    data.put("bio", "");

    Response response = postSeller(data);

    verify(sellerRepo, never()).save(any());
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
