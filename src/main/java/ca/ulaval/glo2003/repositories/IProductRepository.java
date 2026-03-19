package ca.ulaval.glo2003.repositories;

import ca.ulaval.glo2003.models.Product;

import java.util.Collection;

public interface IProductRepository {
    void save(Product product);

    Product get(String id);

    Collection<Product> getAll();

    void clear();
}
