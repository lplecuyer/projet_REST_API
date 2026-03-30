package ca.ulaval.glo2003.repositories.mongo;

import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import ca.ulaval.glo2003.repositories.inmemory.SellerRepository;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;

public class SellerRepositoryMongo implements ISellerRepository {
  private final Datastore datastore;

  public SellerRepositoryMongo(Datastore datastore) {
    this.datastore = datastore;
  }

  @Override
  public void save(Seller seller) {
    datastore.save(seller);
  }

  @Override
  public Seller get(String id) {
    Query<Seller> query = datastore.find(Seller.class);
    query.filter(Filters.eq("id", id));

    return query.first();
  }

  @Override
  public void clear() {}
}
