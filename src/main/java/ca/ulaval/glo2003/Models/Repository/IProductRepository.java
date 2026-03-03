package ca.ulaval.glo2003.Models.Repository;

import ca.ulaval.glo2003.Models.Product;

import java.util.Collection;

public interface IProductRepository {
    void save(Product product);

    Product get(String id);

    Collection<Product> getAll();

    void clear();
}
