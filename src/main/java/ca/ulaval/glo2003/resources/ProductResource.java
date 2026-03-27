package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.services.ProductService;
import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.models.*;
import ca.ulaval.glo2003.assemblers.ProductAssembler;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.Objects;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

  private ProductService productService;

  @Inject
  public ProductResource(ProductService productService) {
    this.productService = productService;
  }

  @POST
  public Response create(ProductAssembler.ProductRequest productRequest, @Context UriInfo uriInfo, @HeaderParam("X-Seller-Id") String sellerId) {

    if (!ProductCategory.categoryExists(productRequest.category())) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(new ErrorResponse("INVALID_PARAMETER", "Category does not exists"))
          .build();
    }

    ProductAssembler.ProductResponse productResponse = productService.addProduct(productRequest, sellerId);

    if (productResponse != null) {
      URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(productResponse.id())).build();
      return Response.created(location).entity(productResponse).build();
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
    ProductAssembler.ProductsResponse productsResponse= productService.getAllProducts(sellerId, title, category, minPrice, maxPrice);
    return Response.ok(productsResponse).build();
  }

  @Path("/{productId}")
  @GET
  public Response getProduct(@PathParam("productId") String productId) {
    ProductAssembler.ProductResponse productResponse = productService.getProduct(productId);

    if (productResponse != null) {
      return Response.status(Response.Status.OK).entity(productResponse).build();
    } else return ErrorResponse.createNotFoundResponse("Product", productId);

  }

  @Path("/{productId}/sell")
  @POST
  public Response sellProduct(ProductAssembler.BuyerUsernameResponse username, @PathParam("productId") String productId, @HeaderParam("X-Seller-Id") String sellerId) {
    if(Objects.equals(username.username(), "")) {
      return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("BAD_REQUEST", "Username is invalid")).build();
    }
    if (Objects.equals(sellerId, "")) {
      return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("BAD_REQUEST", "SellerId is invalid")).build();
    }
    if (productService.getProduct(productId) == null) {
      return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("NOT_FOUND", "Given product does not exists")).build();
    }
    if (productService.getSeller(sellerId) == null) {
      return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("NOT_FOUND", "Given seller does not exists")).build();
    }
    if (!productService.getProductsWithGivenUsernameOfferExists(username.username())) {
      return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("NOT_FOUND", "No offer with given username exists")).build();
    }
    if (productService.getProduct(productId).vendu()) {
      return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse("NOT_PERMITTED","product given is already sold")).build();
    }
    productService.sellProduct(productId);
    return Response.status(Response.Status.OK).entity(new ErrorResponse("OK", "Product successfully sold")).build();
  }

  public void setProductService(ProductService productService) {
    this.productService = productService;
  }
}