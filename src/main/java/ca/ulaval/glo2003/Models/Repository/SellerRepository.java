package ca.ulaval.glo2003.Models.Repository;

import ca.ulaval.glo2003.Models.Seller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public enum SellerRepository implements ISellerRepository {
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
