package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.services.ProductService;
import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.models.*;
import ca.ulaval.glo2003.assemblers.ProductAssembler;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;

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
  public Response create(ProductAssembler.ProductRequest productRequest, @Context UriInfo uriInfo, @HeaderParam("X-Seller-Id") String sellerId){

    if(!ProductCategory.categoryExists(productRequest.category())){
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(new ErrorResponse("INVALID_PARAMETER", "Category does  not exists"))
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
  public Response getProduct(@PathParam("productId") String productId){
    ProductAssembler.ProductResponse productResponse = productService.getProduct(productId);

    if (productResponse != null) {
      return Response.status(Response.Status.OK).entity(productResponse).build();
    }
    else return ErrorResponse.createNotFoundResponse("Product", productId);

  }

  public void setProductService(ProductService productService){
    this.productService = productService;
  }
}