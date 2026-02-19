package ca.ulaval.glo2003.ExceptionHandlers;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {

        String raw = e.getConstraintViolations()
                .iterator()
                .next()
                .getMessage(); // ex: "MISSING_PARAMETER: Name is mandatory"

        String[] parts = raw.split(":", 2);

        String error = parts[0];
        String description = parts.length > 1 ? parts[1].trim() : raw;

        System.out.println(error);

        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(error, description)).build();
    }
}