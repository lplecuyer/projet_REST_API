package ca.ulaval.glo2003.Models;

import ca.ulaval.glo2003.ExceptionHandlers.ErrorResponse;
import ca.ulaval.glo2003.ExceptionHandlers.ProcessingExceptionMapper;
import ca.ulaval.glo2003.ExceptionHandlers.ValidationExceptionMapper;
import ca.ulaval.glo2003.Resources.ProductResource;
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
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductIntegrationTest extends JerseyTest {

    private static String seller1Id;
    private static String seller2Id;

    private IProductRepository productRepo;
    private ISellerRepository sellerRepo;

    @Override
    protected URI getBaseUri() {
        return URI.create("http://localhost:0/");
    }

    @Override
    protected Application configure() {
        productRepo = mock(IProductRepository.class);
        sellerRepo = mock(ISellerRepository.class);

        return new ResourceConfig()
                .register(ProductResource.class)
                .register(org.glassfish.jersey.jsonb.JsonBindingFeature.class)
                .register(ValidationExceptionMapper.class)
                .register(ProcessingExceptionMapper.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(productRepo).to(IProductRepository.class);
                        bind(sellerRepo).to(ISellerRepository.class);
                    }
                });
    }

    @BeforeEach
    void startJersey() throws Exception {
        super.setUp();
        reset(productRepo, sellerRepo);

        Seller seller1 = new Seller();
        Seller seller2 = new Seller();

        seller1Id = seller1.getId();
        seller2Id = seller2.getId();

        Product product1 = new Product("iPhone Case", "desc", ProductCategory.electronics, 15);
        product1.setSeller(seller1);

        Product product2 = new Product("Lipstick", "desc", ProductCategory.beauty, 25);
        product2.setSeller(seller1);

        Product product3 = new Product("T-Shirt", "desc", ProductCategory.apparel, 40);
        product3.setSeller(seller2);

        when(sellerRepo.get(seller1Id)).thenReturn(seller1);
        when(sellerRepo.get(seller2Id)).thenReturn(seller2);

        when(productRepo.getAll()).thenReturn(java.util.List.of(product1, product2, product3));
        when(productRepo.get(product1.getId())).thenReturn(product1);
        when(productRepo.get(product2.getId())).thenReturn(product2);
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
    void GivenCorrectProduct_WhenCreateProduct_ThenProductIsCreated() {
        Map<String, Object> data = validProductPayload();

        Response response = postProduct(seller1Id, data);

        assertEquals(201, response.getStatus());
    }

    @Test
    void GivenMissingTitle_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.remove("title");

        Response response = postProduct(seller1Id, data);

        assertEquals(400, response.getStatus());
        verify(productRepo, never()).save(any());

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("MISSING_PARAMETER", err.error);
    }

    @Test
    void GivenInvalidSeller_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();

        Response response = postProduct("invalidId", data);

        assertEquals(404, response.getStatus());
        verify(productRepo, never()).save(any());

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("NOT_FOUND", err.error);
    }

    @Test
    void GivenEmptyDescription_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.put("description", "");

        Response response = postProduct(seller1Id, data);

        assertEquals(400, response.getStatus());
        verify(productRepo, never()).save(any());

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_PARAMETER", err.error);
    }

    @Test
    void GivenInvalidCategory_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.put("category", "not_a_real_category");

        Response response = postProduct(seller1Id, data);

        assertEquals(400, response.getStatus());
        verify(productRepo, never()).save(any());

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_PARAMETER", err.error);
    }

    @Test
    void GivenNegativeSuggestedPrice_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.put("suggestedPrice", -10);

        Response response = postProduct(seller1Id, data);

        assertEquals(400, response.getStatus());
        verify(productRepo, never()).save(any());

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_PARAMETER", err.error);
    }

    @Test
    void GivenNonNumericSuggestedPrice_WhenCreateProduct_Then400() {
        Map<String, Object> data = validProductPayload();
        data.put("suggestedPrice", "banana");

        Response response = postProduct(seller1Id, data);

        assertEquals(400, response.getStatus());
        verify(productRepo, never()).save(any());

        ErrorResponse err = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_PARAMETER", err.error);
    }

    @Test
    void GivenNoFilters_WhenGetAll_ThenReturnsAll() {
        Response response = target("products")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());

        var list = readProducts(response);
        assertEquals(3, list.size());
    }

    @Test
    void GivenSellerId_WhenGetAll_ThenOnlySellerProducts() {
        Response response = target("products")
                .queryParam("sellerId", seller1Id)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());

        var list = readProducts(response);
        assertEquals(2, list.size());
    }

    @Test
    void GivenTitle_WhenGetAll_ThenMatchesTitleContains() {
        Response response = target("products")
                .queryParam("title", "iphone")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());

        var list = readProducts(response);
        assertEquals(1, list.size());
        assertEquals("iPhone Case", list.get(0).get("title"));
    }

    @Test
    void GivenCategory_WhenGetAll_ThenOnlyThatCategory() {
        Response response = target("products")
                .queryParam("category", "beauty") // ou "BEAUTY" selon ton enum
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());

        var list = readProducts(response);
        assertEquals(1, list.size());
        assertEquals("Lipstick", list.get(0).get("title"));
    }

    @Test
    void GivenMinMaxPrice_WhenGetAll_ThenOnlyInRange() {
        Response response = target("products")
                .queryParam("minPrice", 20)
                .queryParam("maxPrice", 40)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());

        var list = readProducts(response);
        assertEquals(2, list.size()); // 25 et 40
    }

    @Test
    void GivenCombinedFilters_WhenGetAll_ThenIntersection() {
        Response response = target("products")
                .queryParam("sellerId", seller1Id)
                .queryParam("category", "beauty") // ou "BEAUTY"
                .queryParam("minPrice", 20)
                .queryParam("maxPrice", 30)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());

        var list = readProducts(response);
        assertEquals(1, list.size());
        assertEquals("Lipstick", list.get(0).get("title"));
    }

    // pour ne pas repeter le POST
    private Response postProduct(String sellerId, Map<String, Object> data) {
        return target("products")
                .request(MediaType.APPLICATION_JSON)
                .header("X-Seller-Id", sellerId)
                .post(Entity.json(data));
    }

    // pour ne pas repeter le GET
    private Response getProducts(Map<String, Object> params) {
        var t = target("products");
        for (var e : params.entrySet()) {
            t = t.queryParam(e.getKey(), e.getValue());
        }
        return t.request(MediaType.APPLICATION_JSON).get();
    }

    // pour ne pas repeter la creation du produit
    private Map<String, Object> validProductPayload() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Valid title");
        data.put("description", "Valid description");
        data.put("category", "beauty");      // adapte si ton API exige "BEAUTY"
        data.put("suggestedPrice", 200);
        return data;
    }


}