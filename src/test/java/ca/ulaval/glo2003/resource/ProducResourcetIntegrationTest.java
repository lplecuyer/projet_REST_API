package ca.ulaval.glo2003.resource;

import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.exceptionhandlers.ProcessingExceptionMapper;
import ca.ulaval.glo2003.exceptionhandlers.ValidationExceptionMapper;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.resources.ProductResource;
import ca.ulaval.glo2003.services.ProductService;
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
        productResponse = ProductAssembler.makeProductResponse(product);
    System.out.println(productResponse);

    }

  @AfterEach
    void stopJersey() throws Exception {
        super.tearDown();
    }

    @SuppressWarnings("unchecked")
    private java.util.List<Map<String, Object>> readProducts(Response response) {
        Map<String, Object> body = response.readEntity(Map.class);
        return (java.util.List<Map<String, Object>>) body.get("products");
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

    private Map<String, Object> validProductPayload() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", VALID_PAYLOAD_TITLE);
        data.put("description", VALID_PAYLOAD_DESCRIPTION);
        data.put("category", VALID_PAYLOAD_CATEGORY);
        data.put("suggestedPrice", VALID_PAYLOAD_PRICE);
        return data;
    }

}