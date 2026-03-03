package ca.ulaval.glo2003.ExceptionHandlers;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {

        // ex: "MISSING_PARAMETER: Name is mandatory"
        String raw = e.getConstraintViolations().isEmpty()
                ? "INVALID_PARAMETER: Validation error"
                : e.getConstraintViolations().iterator().next().getMessage();

        String[] parts = raw.split(":", 2);
        String error = parts[0];
        String description = parts.length > 1 ? parts[1].trim() : raw;

        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(error, description)).build();
    }
}