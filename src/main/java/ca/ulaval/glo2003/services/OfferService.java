package ca.ulaval.glo2003.services;

import ca.ulaval.glo2003.assemblers.OfferAssembler;
import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.models.Offer;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.IProductRepository;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import jakarta.inject.Inject;

public class OfferService {

  private final IProductRepository productRepository;
  private final ISellerRepository sellerRepository;

  @Inject
  public OfferService(IProductRepository prepository, ISellerRepository srepository) {
    this.productRepository = prepository;
    this.sellerRepository = srepository;
  }

  public boolean offerForSameProductAndUserAlreadyExists(String buyerUsername, String productId) {
    Product product = productRepository.get(productId);
    return product.offerForSameProductAndUserAlreadyExists(buyerUsername);
  }

  public boolean offerAmountLessThanProductPrice(OfferAssembler.OfferRequest offerRequest,
                                                 ProductAssembler.ProductResponse productResponse) {
    if(offerRequest.amount() == null) {
      return false;
    }

    return offerRequest.amount() < productResponse.suggestedPrice();
  }

  public OfferAssembler.OfferResponse addOffer(OfferAssembler.OfferRequest offerRequest,
                                               String productId, String buyerUsername) {
    Offer offer = new Offer(offerRequest.amount(), offerRequest.message(), buyerUsername);
    Product product = productRepository.get(productId);
    product.addOffer(offer);
    productRepository.save(product);
    return OfferAssembler.makeOfferResponse(offer);
  }

  public ProductAssembler.ProductResponse getProduct(String id){
    Product product = productRepository.get(id);
    if(product != null) {
      Seller seller = sellerRepository.get(product.getSellerId());
      return ProductAssembler.makeProductResponse(product, seller);
    }
    else return null;
  }
}
