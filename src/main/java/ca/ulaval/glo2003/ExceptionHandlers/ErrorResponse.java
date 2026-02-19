package ca.ulaval.glo2003.ExceptionHandlers;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.ws.rs.core.Response;

@JsonbPropertyOrder({ "error", "description" })
public class ErrorResponse {
    public String error;
    public String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    //Retourne une Response NOT_FOUND selon le nom de l'objet et son id
    public static Response createNotFoundResponse(String objectType, int sellerId) {
        String message = objectType + " with ID " + sellerId + " does not exist";
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("NOT_FOUND", message)).build();
    }
}