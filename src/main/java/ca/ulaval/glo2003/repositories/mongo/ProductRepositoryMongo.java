package ca.ulaval.glo2003.repositories.mongo;

import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.IProductRepository;
import ca.ulaval.glo2003.repositories.mongo.SellerRepositoryMongo;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;

import java.util.Collection;
import java.util.List;

public class ProductRepositoryMongo implements IProductRepository {
  private final Datastore datastore;

  public ProductRepositoryMongo(Datastore datastore) {
    this.datastore = datastore;
  }

  @Override
  public void save(Product product) {
    datastore.save(product);
  }

  @Override
  public Product get(String id) {
    Query<Product> query = datastore.find(Product.class);
    query.filter(Filters.eq("id", id));

    return query.first();
  }

  @Override
  public Collection<Product> getAll() {
    return datastore.find(Product.class)
        .iterator()
        .toList();
  }

  @Override
  public void clear() {}
}
