package ca.ulaval.glo2003.application;

import ca.ulaval.glo2003.assemblers.OfferAssembler;
import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.assemblers.SellerAssembler;
import ca.ulaval.glo2003.models.Offer;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.ProductRepository;
import ca.ulaval.glo2003.repositories.SellerRepository;
import java.util.List;
import java.util.Objects;

public class ServerApplication {

  private SellerRepository sellerRepository = new SellerRepository();
  private ProductRepository productRepository = new ProductRepository();

  public ServerApplication(){

  }

  public SellerAssembler.SellerResponse addSeller(SellerAssembler.SellerRequest sellerRequest){
    Seller seller = new Seller(sellerRequest.name(),
        sellerRequest.birthdate(),
        sellerRequest.email(),
        sellerRequest.phoneNumber(),
        sellerRequest.bio());
    sellerRepository.save(seller);
    return SellerAssembler.makeSellerResponse(seller);
  }

  public SellerAssembler.SellerResponse getSeller(String id){
    Seller seller = sellerRepository.get(id);
    if(seller != null)
      return SellerAssembler.makeSellerResponse(seller);
    else return null;
  }

  public ProductAssembler.ProductResponse addProduct(ProductAssembler.ProductRequest productRequest, String sellerId){
    Seller seller = sellerRepository.get(sellerId);
    Product product = new Product(productRequest.title(), productRequest.description(),
                              productRequest.category(), productRequest.suggestedPrice());
    if (seller != null) {
      registerProduct(product, seller);
      return ProductAssembler.makeProductResponse(product);
    }
    else return null;
  }

  public void registerProduct(Product product, Seller seller) {
    product.setSeller(seller);
    seller.addProduct(product);
    productRepository.save(product);
  }

  public ProductAssembler.ProductResponse getProduct(String id){
    Product product = productRepository.get(id);
    if(product != null)
      return ProductAssembler.makeProductResponse(product);
    else return null;
  }

  public ProductAssembler.ProductsResponse getAllProducts(String sellerId, String title,
                                                          ProductCategory category, float minPrice, float maxPrice){
    List<ProductAssembler.ProductResponse> products = productRepository.getAll().stream()
        .filter(product -> sellerId == null || Objects.equals(product.getSeller().getId(), sellerId))
        .filter(product -> title == null || product.getTitle().toLowerCase().contains(title.toLowerCase()))
        .filter(product -> category == null || product.getCategory() == category)
        .filter(product -> product.getSuggestedPrice() >= minPrice && product.getSuggestedPrice() <= maxPrice)
        .map(ProductAssembler::makeProductResponse)
        .toList();
    return new ProductAssembler.ProductsResponse(products);
  }

  public void setSellerRepository(SellerRepository sellerRepository){
    this.sellerRepository = sellerRepository;
  }

  public void setProductRepository(ProductRepository productRepository){
    this.productRepository = productRepository;
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
}
