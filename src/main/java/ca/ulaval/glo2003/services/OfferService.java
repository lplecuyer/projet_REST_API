package ca.ulaval.glo2003.services;

import ca.ulaval.glo2003.assemblers.OfferAssembler;
import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.models.Offer;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.repositories.IProductRepository;
import jakarta.inject.Inject;

public class OfferService {

  private IProductRepository productRepository;

  @Inject
  public OfferService(IProductRepository prepository) {
    this.productRepository = prepository;
  }

  public boolean offerForSameProductAndUserAlreadyExists(String buyerUsername, String productId) {
    Product product = productRepository.get(productId);
    return product.offerForSameProductAndUserAlreadyExists(buyerUsername);
  }

  public OfferAssembler.OfferResponse addOffer(OfferAssembler.OfferRequest offerRequest,
                                               String productId, String buyerUsername) {
    Offer offer = new Offer(offerRequest.amount(), offerRequest.message(), buyerUsername);
    Product product = productRepository.get(productId);
    product.addOffer(offer);
    return OfferAssembler.makeOfferResponse(offer);
  }

  public ProductAssembler.ProductResponse getProduct(String id){
    Product product = productRepository.get(id);
    if(product != null)
      return ProductAssembler.makeProductResponse(product);
    else return null;
  }

  public void setProductRepository(IProductRepository productRepository) {
    this.productRepository = productRepository;
  }
}
