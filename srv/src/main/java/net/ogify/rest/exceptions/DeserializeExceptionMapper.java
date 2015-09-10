package net.ogify.rest.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.ogify.rest.elements.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by melges on 11.02.2015.
 */
@Provider
public class DeserializeExceptionMapper implements ExceptionMapper<JsonProcessingException> {
    @Override
    public Response toResponse(JsonProcessingException exception) {
        ErrorResponse response =
                new ErrorResponse(exception.getClass().getSimpleName(), exception.getLocalizedMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }
}
