package ca.ulaval.glo2003.repositories;

import ca.ulaval.glo2003.models.Seller;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class SellerRepository implements ISellerRepository{

    public SellerRepository(){}

    private final static Map<String, Seller> sellers = new ConcurrentHashMap<>();

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
