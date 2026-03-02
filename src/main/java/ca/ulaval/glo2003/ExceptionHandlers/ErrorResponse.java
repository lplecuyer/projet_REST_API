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

    public static Response createNotFoundResponse(String objectType, String Id) {
        String message = objectType + " with ID " + Id + " does not exist";
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("NOT_FOUND", message)).build();
    }
}