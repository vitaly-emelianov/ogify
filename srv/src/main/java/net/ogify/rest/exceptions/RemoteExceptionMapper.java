package net.ogify.rest.exceptions;

import net.ogify.rest.elements.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.rmi.RemoteException;

/**
 * Created by melges on 17.12.14.
 */
@Provider
public class RemoteExceptionMapper implements ExceptionMapper<RemoteException> {
    @Override
    public Response toResponse(RemoteException exception) {
        ErrorResponse response =
                new ErrorResponse(exception.getClass().getSimpleName(), exception.getLocalizedMessage());
        return Response.status(Response.Status.BAD_GATEWAY).entity(response).build();
    }
}
