package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.ExceptionHandlers.ErrorResponse;
import ca.ulaval.glo2003.Models.*;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;

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
                           @HeaderParam("X-Seller-Id") int sellerId){
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
        Seller seller = SellerRepository.INSTANCE.get(sellerId);

        if (seller != null) {
            registerProduct(product, seller);
            return Response.created(location).entity(
                    ProductRecordFactory.makeProductRecord(product)).build();
        }
        else return ErrorResponse.createNotFoundResponse("Seller", sellerId);
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

}
