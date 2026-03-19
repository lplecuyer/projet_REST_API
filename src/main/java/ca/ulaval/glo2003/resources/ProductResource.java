package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.models.*;
import ca.ulaval.glo2003.repositories.IProductRepository;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import ca.ulaval.glo2003.assemblers.ProductAssembler;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.Collection;
import java.util.Objects;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

  private final IProductRepository productRepository;
  private final ISellerRepository sellerRepository;

  @Inject
  public ProductResource(IProductRepository products, ISellerRepository sellers) {
    this.productRepository = products;
    this.sellerRepository = sellers;
  }


  @POST
  public Response create(@Valid Product product, @Context UriInfo uriInfo, @HeaderParam("X-Seller-Id") String sellerId){
    URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
    Seller seller = sellerRepository.get(sellerId);

    if (seller != null) {
      seller.registerProduct(product);
      return Response.created(location).entity(
          ProductAssembler.makeProductResponse(product)).build();
    }
    else return ErrorResponse.createNotFoundResponse("Seller", sellerId);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAll(
      @Context UriInfo uriInfo,
      @QueryParam("category") ProductCategory category,
      @QueryParam("sellerId") String sellerId,
      @QueryParam("title") String title,
      @QueryParam("minPrice") @DefaultValue("0") float minPrice,
      @QueryParam("maxPrice") @DefaultValue("3.4028235E38") float maxPrice
  )
  {

    Collection<ProductAssembler.ProductResponse> products = productRepository.getAll().stream()
        .filter(product -> sellerId == null || Objects.equals(product.getSeller().getId(), sellerId))
        .filter(product -> title == null || product.getTitle().toLowerCase().contains(title.toLowerCase()))
        .filter(product -> category == null || product.getCategory() == category)
        .filter(product -> product.getSuggestedPrice() >= minPrice && product.getSuggestedPrice() <= maxPrice)
        .map(ProductAssembler::makeProductResponse)
        .toList();


    return Response.ok(new ProductAssembler.ProductsResponse(products)).build();
  }

  @Path("/{productId}")
  @GET
  public Response getProduct(@PathParam("productId") String productId){
    Product product = productRepository.get(productId);

    if (product != null) {
      return Response.status(Response.Status.OK).entity(
          ProductAssembler.makeProductResponse(product)).build();
    }
    else return ErrorResponse.createNotFoundResponse("Product", productId);

  }

}