package ca.ulaval.glo2003.Models;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

// Cette classe permet de stocker les produits cote domaine par hashmap
public enum ProductRepository {
    INSTANCE;

    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public void save(Product product) {
        products.put(product.getId(), product);
    }

    public Product get(String id) {
        return products.get(id);
    }
}
