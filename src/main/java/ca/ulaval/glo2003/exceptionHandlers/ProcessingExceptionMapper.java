package ca.ulaval.glo2003.exceptionHandlers;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {

    @Override
    public Response toResponse(ProcessingException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(
                        "INVALID_PARAMETER",
                        "Malformed JSON or invalid field value"
                ))
                .build();
    }
}