package ca.ulaval.glo2003.services;

import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.ProductRepository;
import ca.ulaval.glo2003.repositories.SellerRepository;

import java.util.List;
import java.util.Objects;

public class ProductService {

  private ProductRepository productRepository = new ProductRepository();
  private SellerRepository sellerRepository = new SellerRepository();

  public ProductService(){}

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

  public void setProductRepository(ProductRepository productRepository){
    this.productRepository = productRepository;
  }

  public void setSellerRepository(SellerRepository sellerRepository){
    this.sellerRepository = sellerRepository;
  }
}
