package net.ogify.engine.secure;

import org.apache.log4j.Logger;
import net.ogify.engine.secure.exceptions.ForbiddenException;
import net.ogify.engine.secure.exceptions.NotAuthenticatedException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Filter class, jersey invoke filter method of this class before invoke any
 * resource methods.
 * <p/>
 * Class must check is the client which is try to call resource method has a right for that.
 */
@Provider
public class AuthFilter implements ContainerRequestFilter {
    /**
     * Class logger.
     */
    private final static Logger logger = Logger.getLogger(AuthFilter.class);

    /**
     * Field is used for get information from context about invoked web resource.
     */
    @Context
    private ResourceInfo resourceInfo;

    /**
     * Check is client has right to invoke requested method. If client haven't right invoke will be denied and
     * exception will throw.
     *
     * @param requestContext context for get information about client and requested method and other.
     * @throws IOException
     * @throws WebApplicationException
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException, WebApplicationException {
        // Get method which will be invoked by jersey if we don't deny it
        Method resourceMethod = resourceInfo.getResourceMethod();

        if (resourceMethod.isAnnotationPresent(PermitAll.class))
            return; // Simply return and continue work if resource available for all

        if (resourceMethod.isAnnotationPresent(DenyAll.class))
            throw new ForbiddenException("Forbidden. Denied for all.");

        Map<String, Cookie> cookieMap = requestContext.getCookies();
        Cookie vkIdCookie = cookieMap.get(AuthController.USER_ID_COOKIE_NAME);
        if (vkIdCookie == null)
            throw new NotAuthenticatedException("No vkId in cookie.");
        Long vkId;
        try {
            vkId = Long.parseLong(vkIdCookie.getValue());
        } catch (NumberFormatException exception) {
            throw new NotAuthenticatedException("Invalid vkId in cookie");
        }

        Cookie sessionCookie = cookieMap.get(AuthController.SESSION_COOKIE_NAME);
        if (sessionCookie == null)
            throw new NotAuthenticatedException("No session in cookie.");
        String sessionSecret = sessionCookie.getValue();

        if (resourceMethod.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowed = resourceMethod.getAnnotation(RolesAllowed.class);
            String[] allowedRoles = rolesAllowed.value();

            // TODO: Add user group support

            return;
        }

        if (!AuthController.isSessionCorrect(vkId, sessionSecret))
            throw new NotAuthenticatedException("Session incorrect or expired");

    }
}