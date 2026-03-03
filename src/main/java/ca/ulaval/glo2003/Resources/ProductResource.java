package ca.ulaval.glo2003.Resources;

import ca.ulaval.glo2003.ExceptionHandlers.ErrorResponse;
import ca.ulaval.glo2003.Models.*;
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

    //Crée un produit et renvoie le JSON correspondant.
    //Si le sellerId passé en header ne correspond pas à une instance,
    //Response.Status.NOT_FOUND sera envoyée.
    @POST
    public Response create(@Valid Product product,
                           @Context UriInfo uriInfo,
                           @HeaderParam("X-Seller-Id") String sellerId){
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
        Seller seller = SellerRepository.INSTANCE.get(sellerId);
        ProductRepository.INSTANCE.save(product);

        if (seller != null) {
            registerProduct(product, seller);
            return Response.created(location).entity(
                    ProductResponseFactory.makeProductResponse(product)).build();
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

        Collection<ProductRecordFactory.ProductRecord> products = ProductRepository.INSTANCE.getAll().stream()
                .filter(product -> sellerId == null || Objects.equals(product.getSeller().getId(), sellerId))
                .filter(product -> title == null || product.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(product -> category == null || product.getCategory() == category)
                .filter(product -> product.getSuggestedPrice() >= minPrice && product.getSuggestedPrice() <= maxPrice)
                .map(ProductRecordFactory::makeProductRecord)
                .toList();


        return Response.ok(new ProductRecordFactory.ProductsResponse(products)).build();
    }

    // Obtient un Product à partir de son identifiant et
    // retourne un ProductRecord ou un message d'erreur s'il n'est pas trouvé
    @Path("/{productId}")
    @GET
    public Response getProduct(@PathParam("productId") String productId){
        Product product = ProductRepository.INSTANCE.get(productId);

        if (product != null) {
            return Response.status(Response.Status.OK).entity(
                    ProductResponseFactory.makeProductResponse(product)).build();
        }
        else return ErrorResponse.createNotFoundResponse("Product", productId);

    }

    //Enregistre le produit dans le stockage du domaine et
    // fait les liens nécessaires avec son vendeur.
    private void registerProduct(Product product, Seller seller) {
        product.setSeller(seller);
        seller.addProduct(product);
        ProductRepository.INSTANCE.save(product);
    }

}
