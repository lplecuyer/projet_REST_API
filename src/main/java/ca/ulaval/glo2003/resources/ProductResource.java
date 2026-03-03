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
                           @HeaderParam("X-Seller-Id") String sellerId){
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
        Seller seller = SellerRepository.INSTANCE.get(sellerId);

        if (seller != null) {
            seller.registerProduct(product);
            return Response.created(location).entity(
                    ProductResponseFactory.makeProductResponse(product)).build();
        }
        else return ErrorResponse.createNotFoundResponse("Seller", sellerId);
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
}
