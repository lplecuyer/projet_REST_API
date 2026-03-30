package ca.ulaval.glo2003.persistence.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class MorphiaDatastoreProvider {
  private static Datastore datastore;

  public static Datastore getDatastore() {
    if (datastore == null) {
      String mongoUrl = System.getenv("MONGO_CLUSTER_URL");
      String databaseName = System.getenv("MONGO_DATABASE");

      if (mongoUrl == null || databaseName == null) {
        throw new IllegalStateException(
            "MONGO_CLUSTER_URL and MONGO_DATABASE must be set"
        );
      }

      MongoClient client = MongoClients.create(mongoUrl);
      datastore = Morphia.createDatastore(client, databaseName);
    }

    return datastore;
  }
}
