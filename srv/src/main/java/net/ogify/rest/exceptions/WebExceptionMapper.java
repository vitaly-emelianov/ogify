package net.ogify.rest.exceptions;

import net.ogify.rest.elements.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by melges on 08.02.2015.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException exception) {
        ErrorResponse response =
                new ErrorResponse(exception.getResponse().getStatusInfo().toString(),
                        exception.getMessage());
        return Response.status(exception.getResponse().getStatus())
                .entity(response)
                .build();
    }
}
