package ca.ulaval.glo2003.Resources;

import ca.ulaval.glo2003.ExceptionHandlers.ErrorResponse;
import ca.ulaval.glo2003.Models.Seller;
import ca.ulaval.glo2003.Models.SellerRepository;
import ca.ulaval.glo2003.Models.SellerResponseFactory;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;


@Path("/sellers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SellerResource {

  // cree un vendeur et renvoie ses attributs avec le header contenant son id
  @POST
  public Response create(@Valid Seller seller, @Context UriInfo uriInfo) {
    SellerRepository.INSTANCE.save(seller);
    URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(seller.getId())).build();
    return Response.created(location).entity(seller).build();
  }

  // obtient un vendeur à partir de son identifiant et
  // retourne le vendeur ou un message d'erreur s'il n'est pas trouvé
  @Path("/{id}")
  @GET
  public Response getSeller(@PathParam("id") String id) {
    Seller seller = SellerRepository.INSTANCE.get(id);

    if (seller == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(new ErrorResponse("NOT_FOUND", "Seller with id " + id + " not found"))
          .build();
    }

    return Response.status(Response.Status.OK)
        .entity(SellerResponseFactory.makeSellerResponse(seller))
        .build();
  }
}