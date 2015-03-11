package net.ogify.engine.secure.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Exception which indicate that client haven't right to call resource.
 */
public class ForbiddenException extends WebApplicationException {
    /**
     * Create exception without description.
     */
    public ForbiddenException() {
    }

    /**
     * Create exception with description.
     * @param message description message.
     */
    public ForbiddenException(String message) {
        super(message, Response.Status.FORBIDDEN);
    }
}
