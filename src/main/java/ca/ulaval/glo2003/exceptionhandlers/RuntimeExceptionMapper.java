package ca.ulaval.glo2003.exceptionhandlers;

import io.sentry.Sentry;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
  @Override
  public Response toResponse(RuntimeException e) {
    Sentry.captureException(e);
    return Response.status(400).entity(new ErrorResponse(e.getClass().toString(), e.getMessage())).build();
  }
}
