package ca.ulaval.glo2003.services;

import ca.ulaval.glo2003.assemblers.ProductAssembler;
import ca.ulaval.glo2003.models.Product;
import ca.ulaval.glo2003.models.ProductCategory;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.IProductRepository;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class ProductServiceTest {

  private static String SELLER_ID_1_AND_2;
  private static final String PRODUCT_TITLE_1 = "Iphone";
  private static final String PRODUCT_TITLE_2 = "makeup";
  private static final String PRODUCT_TITLE_3 = "Mouse";
  private static final String A_DESCRIPTION = "Peu importe";
  private static final ProductCategory PRODUCT_CATEGORY_1_AND_3 = ProductCategory.electronics;
  private static final ProductCategory PRODUCT_CATEGORY_2 = ProductCategory.beauty;
  private static final float HIGHER_PRICE = 100.0f;
  private static final float LOWER_PRICE = 10.0f;
  private static final float MIDDLE_PRICE = 20.0f;

  private static final Product product1 = new Product(PRODUCT_TITLE_1, A_DESCRIPTION, PRODUCT_CATEGORY_1_AND_3, HIGHER_PRICE);
  private static final Product product2 = new Product(PRODUCT_TITLE_2, A_DESCRIPTION, PRODUCT_CATEGORY_2, LOWER_PRICE);
  private static final Product product3 = new Product(PRODUCT_TITLE_3, A_DESCRIPTION, PRODUCT_CATEGORY_1_AND_3, MIDDLE_PRICE);

  private static final String VALID_SELLER_ID = "23";
  private static final String INEXISTANT_SELLER_ID = "-1";
  private static final String VALID_TITLE = "Iphone";
  private static final String VALID_DESCRIPTION = "Efficace";
  private static final String VALID_CATEGORY = ProductCategory.beauty.toString();
  private static final float VALID_PRICE = 10.0f;
  private static final float INVALID_PRICE = 0.0f;
  private static final String INEXISTiNG_PRODUCT_ID = "-1";
  private static final String EXISTiNG_PRODUCT_ID = "9832";
  private static final ProductAssembler.ProductRequest invalidProductRequest
      = new ProductAssembler.ProductRequest(null,null, VALID_CATEGORY, INVALID_PRICE);
  private static final ProductAssembler.ProductRequest validProductRequest
      = new ProductAssembler.ProductRequest(VALID_TITLE,VALID_DESCRIPTION, VALID_CATEGORY, VALID_PRICE);

  private static ProductService productService;
  private static IProductRepository productRepository;
  private static ISellerRepository sellerRepository;
  private static ProductAssembler.ProductResponse productResponse;

  private static ArrayList<Product> allProducts;

  @BeforeEach
  void setup(){
    productRepository = mock(IProductRepository.class);
    sellerRepository = mock(ISellerRepository.class);
    productService = new ProductService(productRepository, sellerRepository);

    allProducts = new ArrayList<>();
    fillArrayList();

    when(productRepository.getAll()).thenReturn(allProducts);
  }

  private void fillArrayList() {
    Seller seller1 = new Seller();
    SELLER_ID_1_AND_2 = seller1.getId();;
    Seller seller3 = new Seller();
    String SELLER_ID_3 = seller3.getId();
    product1.setSellerId(seller1.getId());
    product2.setSellerId(seller1.getId());
    product3.setSellerId(seller3.getId());
    allProducts.add(product1);
    allProducts.add(product2);
    allProducts.add(product3);

  }

  @Test
  void givenInvalidProductRequest_whenAddProduct_ThenRequestTossed() {

    assertThrows(ConstraintViolationException.class,
        () -> productService.addProduct(invalidProductRequest, VALID_SELLER_ID));

    verify(productRepository, never()).save(any());
  }

  @Test
  void givenInvalidSellerId_whenAddProduct_ThenReturnNull() {

    when(sellerRepository.get(any())).thenReturn(null);
    ProductAssembler.ProductResponse productResponse = productService.addProduct(validProductRequest, INEXISTANT_SELLER_ID);
    assertNull(productResponse);
    verify(productRepository, never()).save(any());
  }

  @Test
  void givenValidParams_whenAddProduct_ThenReturnProductResponse() {
    when(sellerRepository.get(any())).thenReturn(new Seller());
    ProductAssembler.ProductResponse productResponse =
        productService.addProduct(validProductRequest, INEXISTANT_SELLER_ID);

    verify(productRepository, times(1)).save(any());

    Assertions.assertNotNull(productResponse);
  }

  @Test
  void givenInexistantPrductId_whengetProduct_ThenReturnNull() {
    when(productRepository.get(INEXISTiNG_PRODUCT_ID)).thenReturn(null);
    ProductAssembler.ProductResponse productResponse =
        productService.getProduct(INEXISTiNG_PRODUCT_ID);

    Assertions.assertNull(productResponse);
  }

  @Test
  void givenExistantPrductId_whengetProduct_ThenReturnProduct() {
    Product product = new Product();
    product.setSellerId(new Seller().getId());
    when(productRepository.get(EXISTiNG_PRODUCT_ID)).thenReturn(product);
    ProductAssembler.ProductResponse productResponse =
        productService.getProduct(EXISTiNG_PRODUCT_ID);

    Assertions.assertNotNull(productResponse);
  }

  //productService.getAllProducts(sellerId, title, category, minPrice, maxPrice);
  @Test
  void givenNullParameters_whengetAllProducts_thenReturnsAllProducts() {
    when(productRepository.getAll()).thenReturn(allProducts);
    Collection<ProductAssembler.ProductResponse> filteredProducts
        = productService.getAllProducts(null, null, null, 0.0f, 9999999.0f).products();
    int expectedNumberOfItems = allProducts.size();
    int actualNumberOfItems = filteredProducts.size();
    assertEquals(expectedNumberOfItems, actualNumberOfItems);
  }

  @Test
  void givenTitle_whengetAllProducts_thenReturnsProductWithCorrespondingTitle() {
    when(productRepository.getAll()).thenReturn(allProducts);
    Collection<ProductAssembler.ProductResponse> filteredProducts
        = productService.getAllProducts(null, PRODUCT_TITLE_1, null, 0.0f, 9999999.0f).products();
    int expectedNumberOfItems = 1;
    int actualNumberOfItems = filteredProducts.size();
    assertEquals(expectedNumberOfItems, actualNumberOfItems);
  }

  @Test
  void givenCategory_whengetAllProducts_thenReturnsProductWithCorrespondingCategory() {
    when(productRepository.getAll()).thenReturn(allProducts);
    Collection<ProductAssembler.ProductResponse> filteredProducts
        = productService.getAllProducts(null, null, PRODUCT_CATEGORY_1_AND_3, 0.0f, 9999999.0f).products();
    int expectedNumberOfItems = 2;
    int actualNumberOfItems = filteredProducts.size();
    assertEquals(expectedNumberOfItems, actualNumberOfItems);
  }

  @Test
  void givenSellerId_whengetAllProducts_thenReturnsProductWithCorrespondingSellerId() {
    when(productRepository.getAll()).thenReturn(allProducts);
    Collection<ProductAssembler.ProductResponse> filteredProducts
        = productService.getAllProducts(SELLER_ID_1_AND_2, null, null, 0.0f, 9999999.0f).products();
    int expectedNumberOfItems = 2;
    int actualNumberOfItems = filteredProducts.size();
    assertEquals(expectedNumberOfItems, actualNumberOfItems);
  }

  @Test
  void givenMinPrice_whengetAllProducts_thenReturnsProductWithHigherPrice() {
    when(productRepository.getAll()).thenReturn(allProducts);
    Collection<ProductAssembler.ProductResponse> filteredProducts
        = productService.getAllProducts(null, null, null, LOWER_PRICE +0.1f, 9999999.0f).products();
    int expectedNumberOfItems = 2;
    int actualNumberOfItems = filteredProducts.size();
    assertEquals(expectedNumberOfItems, actualNumberOfItems);
  }

  @Test
  void givenMaxPrice_whengetAllProducts_thenReturnsProductWithLowerPrice() {
    when(productRepository.getAll()).thenReturn(allProducts);
    Collection<ProductAssembler.ProductResponse> filteredProducts
        = productService.getAllProducts(null, null, null, 0.0f, HIGHER_PRICE -0.1f).products();
    int expectedNumberOfItems = 2;
    int actualNumberOfItems = filteredProducts.size();
    assertEquals(expectedNumberOfItems, actualNumberOfItems);
  }

  @Test
  void givenAllParameters_whengetAllProducts_thenReturnsFilteredList() {
    when(productRepository.getAll()).thenReturn(allProducts);
    Collection<ProductAssembler.ProductResponse> filteredProducts
        = productService.getAllProducts(SELLER_ID_1_AND_2, null, PRODUCT_CATEGORY_1_AND_3, LOWER_PRICE +1.0f, 99999999.0f).products();
    int expectedNumberOfItems = 1;
    int actualNumberOfItems = filteredProducts.size();
    assertEquals(expectedNumberOfItems, actualNumberOfItems);

    filteredProducts
        = productService.getAllProducts(SELLER_ID_1_AND_2, null, null, 0.0f, HIGHER_PRICE -0.1f).products();
    actualNumberOfItems = filteredProducts.size();
    assertEquals(expectedNumberOfItems, actualNumberOfItems);
  }



}