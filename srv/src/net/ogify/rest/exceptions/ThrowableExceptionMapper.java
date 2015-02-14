package net.ogify.rest.exceptions;

import net.ogify.rest.elements.ErrorResponse;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Mapper for catch all internal exceptions. All uncatched exception will be processed by this
 * class.
 *
 * @author Morgen Matvey
 */
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    private final static Logger logger = Logger.getLogger(ThrowableExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        logger.error("There are unresolved exception!", exception);
        ErrorResponse response =
                new ErrorResponse("InternalServerErrorException", "Internal problem on server, try later.");
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response).build();
    }
}
