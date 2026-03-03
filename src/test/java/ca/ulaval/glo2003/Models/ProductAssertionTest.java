package ca.ulaval.glo2003.Models;

import ca.ulaval.glo2003.ExceptionHandlers.ProcessingExceptionMapper;
import ca.ulaval.glo2003.ExceptionHandlers.ValidationExceptionMapper;
import ca.ulaval.glo2003.Resources.ProductResource;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductAssertionTest extends JerseyTest {

    private static String seller1Id;
    private static String seller2Id;

    @Override
    protected URI getBaseUri() {
        return URI.create("http://localhost:0/");
    }

    @Override
    protected Application configure() {
        return new ResourceConfig()
                .register(ProductResource.class)
                .register(org.glassfish.jersey.jsonb.JsonBindingFeature.class)
                .register(ValidationExceptionMapper.class)
                .register(ProcessingExceptionMapper.class);
    }

    @BeforeEach
    void startJersey() throws Exception {
        super.setUp();

        SellerRepository.INSTANCE.clear();
        ProductRepository.INSTANCE.clear();

        Seller s1 = new Seller();
        Seller s2 = new Seller();

        SellerRepository.INSTANCE.save(s1);
        SellerRepository.INSTANCE.save(s2);

        seller1Id = s1.getId();
        seller2Id = s2.getId();

        ProductRepository.INSTANCE.save(
                new Product(
                        "Correct test product 1",
                        "first correct product and belongs to Seller 1",
                        ProductCategory.beauty,
                        200
                )
        );

        ProductRepository.INSTANCE.save(
                new Product(
                        "Correct test product 2",
                        "second correct product and belongs to Seller 2",
                        ProductCategory.apparel,
                        500
                )
        );

        ProductRepository.INSTANCE.save(
                new Product(
                        "Correct test product 3",
                        "third correct product and belongs to Seller 2",
                        ProductCategory.electronics,
                        1000
                )
        );
    }

    @AfterEach
    void stopJersey() throws Exception {
        super.tearDown();
    }

    @Test
    void GivenCorrectProduct_WhenCreateProduct_ThenProductIsCreated() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "New test product");
        data.put("description", "created by POST");
        data.put("category", "beauty");
        data.put("suggestedPrice", 200);

        Response response = target("products")
                .request(MediaType.APPLICATION_JSON)
                .header("X-Seller-Id", seller1Id)
                .post(Entity.json(data));

        assertEquals(201, response.getStatus());
    }
}