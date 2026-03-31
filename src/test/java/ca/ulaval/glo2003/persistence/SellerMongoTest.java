package ca.ulaval.glo2003.persistence;

import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.mongo.SellerRepositoryMongo;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class SellerMongoTest {

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8");

  private MongoClient mongoClient;
  private Datastore datastore;
  private SellerRepositoryMongo sellerRepo;

  @BeforeEach
  void setUp() {
    mongoClient = MongoClients.create(mongoDBContainer.getConnectionString());
    datastore = Morphia.createDatastore(mongoClient, "test-db");
    sellerRepo = new SellerRepositoryMongo(datastore);
  }

  @AfterEach
  void tearDown() {
    datastore.getDatabase().drop();
    mongoClient.close();
  }

  @Test
  void givenValidSeller_whenSaving_thenSellerIsPersisted() {
    Seller seller = sellerMaker(1);

    sellerRepo.save(seller);
    Seller savedSeller = sellerRepo.get(seller.getId());

    assertNotNull(savedSeller);
  }

  @Test
  void givenNonExistingSellerId_whenGetting_thenReturnsNull() {
    String nonExistingSellerId = "does-not-exist";

    Seller seller = sellerRepo.get(nonExistingSellerId);

    assertNull(seller);
  }

  @Test
  void givenExistingSeller_whenGettingById_thenReturnsCorrectSeller() {
    Seller seller = sellerMaker(1);
    sellerRepo.save(seller);

    Seller foundSeller = sellerRepo.get(seller.getId());

    assertNotNull(foundSeller);
    assertEquals(seller.getId(), foundSeller.getId());
    assertEquals(seller.getName(), foundSeller.getName());
  }

  @Test
  void givenSellerWithAllFields_whenSaving_thenAllFieldsArePersisted() {
    Seller seller = sellerMaker(1);

    sellerRepo.save(seller);
    Seller savedSeller = sellerRepo.get(seller.getId());

    assertNotNull(savedSeller);
    assertEquals(seller.getId(), savedSeller.getId());
    assertEquals(seller.getName(), savedSeller.getName());
    assertEquals(seller.getEmail(), savedSeller.getEmail());
    assertEquals(seller.getBirthdate(), savedSeller.getBirthdate());
  }

  Seller sellerMaker(int id) {
    return switch (id) {
      case 1 -> new Seller("Louis", "1996-12-25", "louis@email.com", "888-888-8888", "tres cool");
      case 2 -> new Seller("Alice", "2001-09-11", "alice@email.com", "999-999-9999", "tres cool des fois");
      default -> null;
    };
  }
}