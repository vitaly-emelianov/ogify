package net.ogify.engine.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by melge on 25.10.2015.
 */
public class TechnicalAuthException extends WebApplicationException {
    public TechnicalAuthException() {
    }

    public TechnicalAuthException(String message) {
        super(message);
    }

    public TechnicalAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public TechnicalAuthException(Throwable cause, Response.Status status) throws IllegalArgumentException {
        super(cause, status);
    }

    public TechnicalAuthException(String message, Throwable cause, Response.Status status) throws IllegalArgumentException {
        super(message, cause, status);
    }
}
