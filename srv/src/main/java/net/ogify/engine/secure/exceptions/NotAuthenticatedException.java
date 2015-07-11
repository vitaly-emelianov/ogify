package net.ogify.engine.secure.exceptions;

import javax.ws.rs.WebApplicationException;

/**
 * Exception indicate that client not authenticated, or auth data is incorrect.
 */
public class NotAuthenticatedException extends WebApplicationException {
    /**
     * Default constructor, no description in exception will be.
     */
    public NotAuthenticatedException() {
    }

    /**
     * Create object which have description.
     * @param message description.
     */
    public NotAuthenticatedException(String message) {
        super(message);
    }
}
