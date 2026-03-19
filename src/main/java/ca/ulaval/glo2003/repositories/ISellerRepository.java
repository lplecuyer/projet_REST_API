package ca.ulaval.glo2003.repositories;

import ca.ulaval.glo2003.models.Seller;

public interface ISellerRepository {

    void save(Seller seller);

    Seller get(String id);

    void clear();
}
