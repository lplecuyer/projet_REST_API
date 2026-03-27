package ca.ulaval.glo2003.services;

import ca.ulaval.glo2003.assemblers.ProductAssembler;
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

  public ProductAssembler.ProductResponse addProduct(ProductAssembler.ProductRequest productRequest, String sellerId){
    Seller seller = sellerRepository.get(sellerId);
    Product product = new Product(productRequest.title(), productRequest.description(),
       ProductCategory.valueOf(productRequest.category()), productRequest.suggestedPrice());
    if (seller != null) {
      registerProduct(product, seller);
      return ProductAssembler.makeProductResponse(product);
    }
    else return null;
  }

  public void registerProduct(Product product, Seller seller) {
    product.setSellerId(seller.getId());
    seller.addProduct(product);
    sellerRepository.save(seller);
    productRepository.save(product);
  }

  public ProductAssembler.ProductResponse getProduct(String id){
    Product product = productRepository.get(id);
    if(product != null)
      return ProductAssembler.makeProductResponse(product);
    else return null;
  }

  public ProductAssembler.ProductsResponse getAllProducts(String sellerId, String title,
                                                          ProductCategory category, float minPrice, float maxPrice, String description) {
    List<ProductAssembler.ProductResponse> products = productRepository.getAll().stream()
        .filter(product -> sellerId == null || Objects.equals(product.getSellerId(), sellerId))
        .filter(product -> title == null || product.getTitle().toLowerCase().contains(title.toLowerCase()))
        .filter(product -> category == null || product.getCategory() == category)
        .filter(product -> product.getSuggestedPrice() >= minPrice && product.getSuggestedPrice() <= maxPrice)
        .map(ProductAssembler::makeProductResponse)
        .toList();
    return new ProductAssembler.ProductsResponse(products);
  }
}
