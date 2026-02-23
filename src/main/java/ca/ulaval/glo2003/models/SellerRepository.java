package ca.ulaval.glo2003.models;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

// Cette classe permet de stocker les vendeurs cote domaine par hashmap
public enum SellerRepository {
    INSTANCE;

    private final Map<Integer, Seller> sellers = new ConcurrentHashMap<>();

    public void save(Seller seller) {
        sellers.put(seller.getId(), seller);
    }

    public Seller get(int id) {
        return sellers.get(id);
    }
}
