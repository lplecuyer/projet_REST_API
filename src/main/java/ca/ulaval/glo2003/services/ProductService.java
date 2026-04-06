package ca.ulaval.glo2003.services;

import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.assemblers.SellerAssembler;
import ca.ulaval.glo2003.models.*;
import ca.ulaval.glo2003.repositories.IProductRepository;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

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
    } else return null;
  }

  public void registerProduct(Product product, Seller seller) {
    product.setSellerId(seller.getId());
    seller.addProduct(product);
    sellerRepository.save(seller);
    productRepository.save(product);
  }

  public ProductAssembler.ProductResponse getProduct(String id) {
    Product product = productRepository.get(id);

    if (product != null) {
      Seller seller = sellerRepository.get(product.getSellerId());
      return ProductAssembler.makeProductResponse(product, seller);
    } else return null;
  }

  public SellerAssembler.SellerResponse getSeller(String id) {
    Seller seller = sellerRepository.get(id);
    if (seller != null)
      return SellerAssembler.makeSellerResponse(seller);
    else return null;
  }

  public void sellProduct(String id, String buyerUsername) {
    Product product = productRepository.get(id);
    product.setVendu(true);

    Offer selectedOffer = product.getOffers()
        .stream()
        .filter(o -> o.getBuyerUsername().equals(buyerUsername))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Offer not found for this buyer"));

    product.setSelectedOffer(selectedOffer);
  }

  public ProductAssembler.ProductsResponse getAllProducts(String sellerId, String title,
                                                          ProductCategory category, float minPrice, float maxPrice) {
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

  public ProductAssembler.ProductResponse addReview(
      String productId,
      ProductAssembler.@Valid ReviewRequest reviewRequest,
      String buyerUsername
  ) {
    Product product = productRepository.get(productId);

    Review review = new Review(
        reviewRequest.rating(),
        reviewRequest.comment(),
        buyerUsername,
        productId
    );

    product.setReview(review);
    productRepository.save(product);

    Seller seller = sellerRepository.get(product.getSellerId());

    return ProductAssembler.makeProductResponse(product, seller);
  }
}
