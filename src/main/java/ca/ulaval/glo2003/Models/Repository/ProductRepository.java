package ca.ulaval.glo2003.Models.Repository;

import ca.ulaval.glo2003.Models.Product;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ProductRepository implements IProductRepository {
    INSTANCE;

    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public void save(Product product) {
        products.put(product.getId(), product);
    }

    public Product get(String id) {
        return products.get(id);
    }

    public Collection<Product> getAll() {return products.values();}

    public void clear() {
        products.clear();
    }
}
