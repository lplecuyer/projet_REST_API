package ca.ulaval.glo2003.services;

import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.assemblers.SellerAssembler;
import ca.ulaval.glo2003.models.Offer;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.IProductRepository;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Objects;

public class ProductService {

  private final IProductRepository productRepository;
  private final ISellerRepository sellerRepository;

  @Inject
  public ProductService(IProductRepository productRepository, ISellerRepository sellerRepository) {
    this.productRepository = productRepository;
    this.sellerRepository = sellerRepository;
  }

  public ProductAssembler.ProductResponse addProduct(ProductAssembler.ProductRequest productRequest, String sellerId) {
    Seller seller = sellerRepository.get(sellerId);
    Product product = new Product(productRequest.title(), productRequest.description(),
        ProductCategory.valueOf(productRequest.category()), productRequest.suggestedPrice());
    if (seller != null) {
      registerProduct(product, seller);
      return ProductAssembler.makeProductResponse(product, seller);
    }
    else return null;
  }

  public void registerProduct(Product product, Seller seller) {
    product.setSellerId(seller.getId());
    seller.addProduct(product);
    sellerRepository.save(seller);
    productRepository.save(product);
  }

  public ProductAssembler.ProductResponse getProduct(String id) {
    Product product = productRepository.get(id);
    Seller seller  = sellerRepository.get(product.getSellerId());
    if(product != null)
      return ProductAssembler.makeProductResponse(product, seller);
    else return null;
  }

  public SellerAssembler.SellerResponse getSeller(String id) {
    Seller seller = sellerRepository.get(id);
    if (seller != null)
      return SellerAssembler.makeSellerResponse(seller);
    else return null;
  }

  public void sellProduct(String id) {
    productRepository.get(id).setVendu(true);
  }

  public ProductAssembler.ProductsResponse getAllProducts(String sellerId, String title,
                                                          ProductCategory category, float minPrice, float maxPrice){
    List<ProductAssembler.ProductResponse> products = productRepository.getAll().stream()
        .filter(product -> sellerId == null || Objects.equals(product.getSellerId(), sellerId))
        .filter(product -> title == null || product.getTitle().toLowerCase().contains(title.toLowerCase()))
        .filter(product -> category == null || product.getCategory() == category)
        .filter(product -> product.getSuggestedPrice() >= minPrice && product.getSuggestedPrice() <= maxPrice)
        .map(product -> {
          Seller seller = sellerRepository.get(product.getSellerId());
          return ProductAssembler.makeProductResponse(product, seller);
        })
        .toList();
    return new ProductAssembler.ProductsResponse(products);
  }

  public boolean BuyerHasOfferOnProduct(String username, String productId) {
    return productRepository.get(productId).offerForSameProductAndUserAlreadyExists(username);
  }

  public void setProductRepository(IProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public void setSellerRepository(ISellerRepository sellerRepository) {
    this.sellerRepository = sellerRepository;
  }
}
