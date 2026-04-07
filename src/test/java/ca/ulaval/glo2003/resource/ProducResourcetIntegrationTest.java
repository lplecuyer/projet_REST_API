package ca.ulaval.glo2003.resource;

import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.exceptionhandlers.ProcessingExceptionMapper;
import ca.ulaval.glo2003.exceptionhandlers.ValidationExceptionMapper;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Review;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.resources.ProductResource;
import ca.ulaval.glo2003.services.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.WebApplicationException;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProducResourcetIntegrationTest extends JerseyTest {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static Set<ConstraintViolation<Product>> missingParameterError;
    private static Set<ConstraintViolation<Product>> invalidParameterError;

    private static String SELLER_ID_OF_VALID_PRODUCT;
    private static ProductAssembler.ProductResponse productResponse;
    private static String productId1;

    private static final String VALID_BUYER_USERNAME = "buyer123";
    private static final String INVALID_BUYER_USERNAME = "notTheBuyer";

    private final static String SELLER_ID_OF_INVALID_PRODUCT = "99999";
    private static final String VALID_PAYLOAD_TITLE = "Valid title";
    private static final String VALID_PAYLOAD_DESCRIPTION = "Valid description";
    private static final String VALID_PAYLOAD_CATEGORY = "beauty";
    private static final float VALID_PAYLOAD_PRICE = 200.0f;
    private static final String INVALID_SELLER_ID = "invalidId";

    private ProductService productService;

  @Override
    protected URI getBaseUri() {
        return URI.create("http://localhost:0/");
    }

    @Override
    protected Application configure() {
        productService = mock(ProductService.class);
        ProductResource productResource = new ProductResource(productService);
        createMissingParameterError();
        createInvalidParameterError();

        return new ResourceConfig()
                .register(productResource)
                .register(org.glassfish.jersey.jsonb.JsonBindingFeature.class)
                .register(org.glassfish.jersey.server.validation.ValidationFeature.class)
                .register(ValidationExceptionMapper.class)
                .register(ProcessingExceptionMapper.class);
    }

  private void createInvalidParameterError() {
    Product product = new Product();
    product.setDescription("");
    product.setTitle("");
    product.setCategory(ProductCategory.beauty);
    product.setSuggestedPrice(-1F);
    invalidParameterError = validator.validate(product);
  }

  private void createMissingParameterError() {
    Product product = new Product();
    product.setDescription(null);
    product.setTitle(null);
    product.setCategory(null);
    product.setSuggestedPrice(null);
    missingParameterError = validator.validate(product);
  }

  @BeforeEach
    void startJersey() throws Exception {
        super.setUp();
        reset(productService);

        Seller seller1 = new Seller();
        SELLER_ID_OF_VALID_PRODUCT = seller1.getId();
        Product product = new Product();
        productId1 = product.getId();
        product.setSellerId(seller1.getId());
        productResponse = ProductAssembler.makeProductResponse(product, seller1);

        productService.registerProduct(product, seller1);
  }

    @AfterEach
    void stopJersey() throws Exception {
        super.tearDown();
    }

    @Test
    void givenCorrectProduct_WhenCreateProduct_ThenProductIsCreated() {
        Map<String, Object> data = validProductPayload();
        when(productService.addProduct(any(), eq(SELLER_ID_OF_VALID_PRODUCT))).thenReturn(productResponse);

        Response response = postProduct(SELLER_ID_OF_VALID_PRODUCT, data);
        verify(productService, times(1)).addProduct(any(), eq(SELLER_ID_OF_VALID_PRODUCT));

        assertEquals(201, response.getStatus());
    }

    @Test
    void givenMissingTitle_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.remove("title");
        when(productService.addProduct(any(), eq(SELLER_ID_OF_INVALID_PRODUCT)))
          .thenThrow( new ConstraintViolationException(missingParameterError));

        Response response = postProduct(SELLER_ID_OF_INVALID_PRODUCT, data);

        assertEquals(400, response.getStatus());
        verify(productService, times(1)).addProduct(any(), eq(SELLER_ID_OF_INVALID_PRODUCT));

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("MISSING_PARAMETER", err.error);
    }

    @Test
    void givenInvalidSeller_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
      when(productService.addProduct(any(), eq(INVALID_SELLER_ID))).thenReturn(null);

        Response response = postProduct(INVALID_SELLER_ID, data);

        assertEquals(404, response.getStatus());
        verify(productService, times(1)).addProduct(any(), eq(INVALID_SELLER_ID));
        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("NOT_FOUND", err.error);
    }

    @Test
    void givenEmptyDescription_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.put("description", "");
      when(productService.addProduct(any(), eq(SELLER_ID_OF_INVALID_PRODUCT)))
          .thenThrow( new ConstraintViolationException(invalidParameterError));
        Response response = postProduct(SELLER_ID_OF_INVALID_PRODUCT, data);

        assertEquals(400, response.getStatus());
        verify(productService, times(1)).addProduct(any(), eq(SELLER_ID_OF_INVALID_PRODUCT));

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_PARAMETER", err.error);
    }

    @Test
    void givenInvalidCategory_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.put("category", "not_a_real_category");

        Response response = postProduct(SELLER_ID_OF_INVALID_PRODUCT, data);

        assertEquals(400, response.getStatus());
        verify(productService, never()).addProduct(any(), eq(SELLER_ID_OF_INVALID_PRODUCT));

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_PARAMETER", err.error);
    }

    @Test
    void givenNegativeSuggestedPrice_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.put("suggestedPrice", -10);
      when(productService.addProduct(any(), eq(SELLER_ID_OF_INVALID_PRODUCT)))
          .thenThrow( new ConstraintViolationException(invalidParameterError));

        Response response = postProduct(SELLER_ID_OF_INVALID_PRODUCT, data);

        assertEquals(400, response.getStatus());
        verify(productService, times(1)).addProduct(any(), eq(SELLER_ID_OF_INVALID_PRODUCT));

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_PARAMETER", err.error);
    }

    @Test
    void givenNonNumericSuggestedPrice_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.put("suggestedPrice", "banana");

        Response response = postProduct(SELLER_ID_OF_INVALID_PRODUCT, data);

        assertEquals(400, response.getStatus());

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_PARAMETER", err.error);
    }

    @Test
    void givenRequestWithAllParameters_WhenGetAll_ThenReturnOkStatus() {
        Response response = target("products")
                .queryParam("sellerId", SELLER_ID_OF_VALID_PRODUCT)
                .queryParam("category", "beauty") // ou "BEAUTY"
                .queryParam("minPrice", 20)
                .queryParam("maxPrice", 30)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());
    }

  @Test
  public void givenValidSellerId_whenGetSeller_thenOk() {
    when(productService.getProduct(productId1)).thenReturn(productResponse);

    Response response = getProduct(productId1);

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
  }

  @Test
  public void givenInvalidSellerId_whenGetSeller_thenNotFound() {
    when(productService.getProduct(any())).thenReturn(null);

    Response response = getProduct("INVALID_ID");

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertEquals(404, response.getStatus());
  }

  @Test
  void givenValidReviewRequest_WhenCreateReview_ThenReviewIsCreated() {
    ProductAssembler.ReviewResponse reviewResponse = new ProductAssembler.ReviewResponse("Très bon produit", 4.5f, VALID_BUYER_USERNAME);
    ProductAssembler.ProductResponse reviewedProductResponse = productForReviewMaker(reviewResponse);

    when(productService.addReview(anyString(), any(ProductAssembler.ReviewRequest.class), eq(VALID_BUYER_USERNAME)))
        .thenReturn(reviewedProductResponse);

    Response response = postReview(productId1, VALID_BUYER_USERNAME, validReviewPayload());

    assertEquals(201, response.getStatus());
    verify(productService, times(1)).addReview(eq(productId1), any(ProductAssembler.ReviewRequest.class), eq(VALID_BUYER_USERNAME));
  }

  @Test
  void givenMissingBuyerUsernameHeader_WhenCreateReview_Then400() {
    Response response = target("products")
        .path(productId1)
        .path("review")
        .request(MediaType.APPLICATION_JSON)
        .post(Entity.json(validReviewPayload()));

    assertEquals(403, response.getStatus());

    ErrorResponse err = response.readEntity(ErrorResponse.class);
    assertEquals("ACCESS_FORBIDDEN", err.error);
  }

  @Test
  void givenBuyerWhoDidNotBuyProduct_WhenCreateReview_Then403() {
    when(productService.addReview(anyString(), any(ProductAssembler.ReviewRequest.class), eq(INVALID_BUYER_USERNAME)))
        .thenThrow(new WebApplicationException(ErrorResponse.createForbiddenResponse()));

    Response response = postReview(productId1, INVALID_BUYER_USERNAME, validReviewPayload());

    assertEquals(403, response.getStatus());

    ErrorResponse err = response.readEntity(ErrorResponse.class);
    assertEquals("ACCESS_FORBIDDEN", err.error);
  }

  @Test
  void givenInvalidProductId_WhenCreateReview_Then404() {
    when(productService.addReview(anyString(), any(ProductAssembler.ReviewRequest.class), eq(VALID_BUYER_USERNAME)))
        .thenThrow(new WebApplicationException(
            ErrorResponse.createNotFoundResponse("Product", "INVALID_ID")
        ));

    Response response = postReview("INVALID_ID", VALID_BUYER_USERNAME, validReviewPayload());

    assertEquals(404, response.getStatus());

    ErrorResponse err = response.readEntity(ErrorResponse.class);
    assertEquals("NOT_FOUND", err.error);
  }

  @Test
  void givenReviewWithInvalidRating_WhenCreateReview_Then400() {
    Map<String, Object> data = validReviewPayload();
    data.put("rating", 0.5f);

    ProductAssembler.ReviewRequest invalidRequest =
        new ProductAssembler.ReviewRequest("bad", 4.5f);

    Set<ConstraintViolation<ProductAssembler.ReviewRequest>> violations =
        validator.validate(invalidRequest);

    when(productService.addReview(anyString(), any(ProductAssembler.ReviewRequest.class), eq(VALID_BUYER_USERNAME)))
        .thenThrow(new ConstraintViolationException(violations));

    Response response = postReview(productId1, VALID_BUYER_USERNAME, data);

    assertEquals(400, response.getStatus());
  }

  private Response getProduct(String sellerId) {
    return target("products")
        .path(sellerId)
        .request(MediaType.APPLICATION_JSON)
        .get();
  }

  private Response postProduct(String sellerId, Map<String, Object> data) {
      return target("products")
              .request(MediaType.APPLICATION_JSON)
              .header("X-Seller-Id", sellerId)
              .post(Entity.json(data));
  }

  private Response postReview(String productId, String buyerUsername, Map<String, Object> data) {
    return target("products")
        .path(productId)
        .path("review")
        .request(MediaType.APPLICATION_JSON)
        .header("X-Buyer-Username", buyerUsername)
        .post(Entity.json(data));
  }

  private Map<String, Object> validProductPayload() {
      Map<String, Object> data = new HashMap<>();
      data.put("title", VALID_PAYLOAD_TITLE);
      data.put("description", VALID_PAYLOAD_DESCRIPTION);
      data.put("category", VALID_PAYLOAD_CATEGORY);
      data.put("suggestedPrice", VALID_PAYLOAD_PRICE);
      return data;
  }

  private Map<String, Object> validReviewPayload() {
    Map<String, Object> data = new HashMap<>();
    data.put("comment", "Très bon produit");
    data.put("rating", 4.5f);
    return data;
  }

  private ProductAssembler.ProductResponse productForReviewMaker(ProductAssembler.ReviewResponse reviewResponse) {
    return new ProductAssembler.ProductResponse(
        productResponse.id(),
        productResponse.title(),
        productResponse.description(),
        productResponse.category(),
        productResponse.suggestedPrice(),
        LocalDateTime.now(),
        productResponse.seller(),
        productResponse.offers(),
        productResponse.vendu(),
        productResponse.saleStatus(),
        productResponse.selectedOffer(),
        reviewResponse
    );
  }

}