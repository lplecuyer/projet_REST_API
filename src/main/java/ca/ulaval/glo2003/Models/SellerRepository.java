package ca.ulaval.glo2003.Models;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

// Cette classe permet de stocker les vendeurs cote domaine par hashmap
public enum SellerRepository {
    INSTANCE;

    private final Map<String, Seller> sellers = new ConcurrentHashMap<>();

    public void save(Seller seller) {
        sellers.put(seller.getId(), seller);
    }

    public Seller get(String id) {
        return sellers.get(id);
    }

    public void clear() {
        sellers.clear();
    }
}
