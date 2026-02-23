package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.exceptionhandlers.ErrorResponse;
import ca.ulaval.glo2003.models.*;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.Objects;
import java.util.function.Predicate;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    //Crée un produit et renvoie le JSON correspondant.
    //Si le sellerId passé en header ne correspond pas à une instance,
    //Response.Status.NOT_FOUND sera envoyée.
    @POST
    public Response create(@Valid Product product, @Context UriInfo uriInfo, @HeaderParam("X-Seller-Id") int sellerId){
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
        Seller seller = SellerRepository.INSTANCE.get(sellerId);
        ProductRepository.INSTANCE.save(product);

        if (seller != null) {
            registerProduct(product, seller);
            return Response.created(location).entity(ProductRecordFactory.makeProductRecord(product)).build();
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
            @QueryParam("minPrice") float minPrice,
            @QueryParam("maxPrice") float maxPrice
            )
    {
        var products = ProductRepository.INSTANCE.getAll().stream()
                .filter(p -> category == null || p.getCategory() == category)
                .filter(p -> sellerId == null || p.getSeller().getId() == Integer.parseInt(sellerId))
                .filter(p -> title == null || title.isBlank()
                        || p.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(p -> p.getSuggestedPrice() >= minPrice)
                .filter(p -> p.getSuggestedPrice() <= maxPrice)
                .map(ProductRecordFactory::makeProductRecord)
                .toList();


        return Response.ok(new ProductRecordFactory.ProductsResponse(products)).build();
    }

    // Obtient un Product à partir de son identifiant et
    // retourne un ProductRecord ou un message d'erreur s'il n'est pas trouvé
    @Path("/{productId}")
    @GET
    public Response getProduct(@PathParam("productId") int productId){
        Product product = ProductRepository.INSTANCE.get(productId);

        if (product != null) {
            return Response.status(Response.Status.OK).entity(
                    ProductRecordFactory.makeProductRecord(product)).build();
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

    private Response createNotFoundResponse(int sellerId) {
        String message = "Seller with ID " + sellerId + " does not exist";
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("NOT_FOUND", message))
                .build();
    }
}
