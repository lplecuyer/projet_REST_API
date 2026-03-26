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
    String sellerId = product.getSellerId();
    if (sellerId == null) {
      throw new IllegalArgumentException("Product must have a sellerId");
    }

    Seller seller = datastore.find(Seller.class)
        .filter(Filters.eq("id", sellerId))
        .first();

    if (seller == null) {
      throw new IllegalArgumentException("Seller not found for id: " + sellerId);
    }

    seller.addProduct(product);
    datastore.save(seller);
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
