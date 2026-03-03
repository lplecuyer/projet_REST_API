package ca.ulaval.glo2003.Models.Repository;

import ca.ulaval.glo2003.Models.Seller;

public interface ISellerRepository {

    void save(Seller seller);

    Seller get(String id);

    void clear();
}
