package ca.ulaval.glo2003.Models;

import java.util.Collection;

public interface ISellerRepository {

    void save(Seller seller);

    Seller get(String id);

    void clear();
}
