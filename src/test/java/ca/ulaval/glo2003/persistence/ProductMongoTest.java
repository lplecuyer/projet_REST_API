package ca.ulaval.glo2003.persistence;

import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Review;
import ca.ulaval.glo2003.repositories.mongo.ProductRepositoryMongo;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ProductMongoTest {

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8");

  private MongoClient mongoClient;
  private Datastore datastore;
  private ProductRepositoryMongo productRepo;

  @BeforeEach
  void setUp() {
    mongoClient = MongoClients.create(mongoDBContainer.getConnectionString());
    datastore = Morphia.createDatastore(mongoClient, "test-db");
    productRepo = new ProductRepositoryMongo(datastore);
  }

  @AfterEach
  void tearDown() {
    datastore.getDatabase().drop();
    mongoClient.close();
  }

  @Test
  void givenValidProduct_whenSaving_thenProductIsPersisted() {
    Product product = new Product("Laptop", "Bon laptop", ProductCategory.electronics, 1200f);
    product.setSellerId("seller-1");

    productRepo.save(product);
    Product savedProduct = productRepo.get(product.getId());

    assertNotNull(savedProduct);
  }

  @Test
  void givenNonExistingProductId_whenGetting_thenReturnsNull() {
    String nonExistentProductId = "does-not-exist";

    Product product = productRepo.get(nonExistentProductId);

    assertNull(product);
  }

  @Test
  void givenExistingProduct_whenGettingById_thenReturnsCorrectProduct() {
    Product product = new Product("Phone", "Bon phone", ProductCategory.electronics, 800f);
    product.setSellerId("seller-2");

    productRepo.save(product);

    Product foundProduct = productRepo.get(product.getId());

    assertNotNull(foundProduct);
    assertEquals(product.getId(), foundProduct.getId());
    assertEquals(product.getTitle(), foundProduct.getTitle());
  }

  @Test
  void givenNoProducts_whenGettingAll_thenReturnsEmptyList() {
    List<Product> products = List.copyOf(productRepo.getAll());

    assertNotNull(products);
    assertTrue(products.isEmpty());
  }

  @Test
  void givenMultipleProducts_whenGettingAll_thenReturnsAllProducts() {
    Product p1 = new Product("Laptop", "Desc 1", ProductCategory.electronics, 1000f);
    Product p2 = new Product("Ball", "Desc 2", ProductCategory.sports, 50f);

    productRepo.save(p1);
    productRepo.save(p2);

    List<Product> products = List.copyOf(productRepo.getAll());

    assertEquals(2, products.size());
  }

  @Test
  void givenProductWithAllFields_whenSaving_thenAllFieldsArePersisted() {
    Product product = new Product("Tablet", "Bonne tablette", ProductCategory.electronics, 500f);
    product.setSellerId("seller-99");

    productRepo.save(product);
    Product savedProduct = productRepo.get(product.getId());

    assertNotNull(savedProduct);
    assertEquals(product.getId(), savedProduct.getId());
    assertEquals("Tablet", savedProduct.getTitle());
    assertEquals("Bonne tablette", savedProduct.getDescription());
    assertEquals(ProductCategory.electronics, savedProduct.getCategory());
    assertEquals(500f, savedProduct.getSuggestedPrice());
    assertEquals("seller-99", savedProduct.getSellerId());
  }

  @Test
  void givenProductWithOneReview_whenSaving_thenReviewIsPersisted() {
    Product product = new Product("Laptop", "Bon laptop", ProductCategory.electronics, 1200f);
    product.setSellerId("seller-1");

    Review review = new Review(5, "cool beans", "buyer1", product.getId());
    product.setReview(review);

    productRepo.save(product);

    Product savedProduct = productRepo.get(product.getId());

    assertNotNull(savedProduct);
    assertNotNull(savedProduct.getReview());

    assertEquals("buyer1", savedProduct.getReview().getBuyerUsername());
    assertEquals(5, savedProduct.getReview().getRating());
    assertEquals("cool beans", savedProduct.getReview().getComment());
  }

}