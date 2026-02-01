package ca.ulaval.glo2003.resources;

import ca.ulaval.glo2003.ExceptionHandlers.ErrorResponse;
import ca.ulaval.glo2003.Models.Product;
import ca.ulaval.glo2003.Models.Seller;
import ca.ulaval.glo2003.Models.SellerRepository;
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

        Response response;
        if (seller != null) {
            seller.addProduct(product);
            response = Response.created(location).entity(product).build();
        }

        else {
            String message = "Seller with ID " + sellerId + " does not exist";
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NOT_FOUND", message))
                    .build();
        }
        return response;
    }
}
