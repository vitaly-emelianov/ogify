package net.ogify.rest.resources;

import net.ogify.database.entities.SocialNetwork;
import net.ogify.engine.secure.AuthController;
import net.ogify.engine.vkapi.VkAuth;
import net.ogify.engine.vkapi.exceptions.VkSideError;
import net.ogify.rest.elements.SNRequestUri;
import net.ogify.rest.elements.SocialNetworkParam;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by melges.morgen on 15.02.15.
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class AuthResource {

    @Autowired
    VkAuth vkAuth;

    @Autowired
    AuthController authController;

    /**
     * Field storing the session id of the user if the user has no session it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = AuthController.SESSION_COOKIE_NAME)
    private String sessionSecret;

    /**
     * Field storing the vk id of the user if the user not authorized it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = AuthController.USER_ID_COOKIE_NAME)
    private Long userId;

    @Path("/getRequestUri")
    @GET
    @PermitAll
    public SNRequestUri getRequestUri(@Context UriInfo uriInfo,
                                      @NotNull @QueryParam("sn") SocialNetworkParam socialNetwork) {
        URI authRequestUri = uriInfo.getBaseUriBuilder()
                .path(AuthResource.class) // Add class path;
                .build();

        SNRequestUri generatedUriResponse;
        switch(socialNetwork.getValue()) {
            case Vk:
                generatedUriResponse = vkAuth.getClientAuthUri(authRequestUri);
                break;
            case FaceBook:
                generatedUriResponse = new SNRequestUri();
                break;
            default:
                generatedUriResponse = new SNRequestUri();
        }

        return generatedUriResponse;
    }

    /**
     * Method for simplify checking auth status from client side, return 200 OK if user authenticated,
     * or 401 Unauthorized (Error will raised by AuthFilter).

     * @return always return empty OK Response.
     */
    @Path("/isAuthenticated")
    @GET
    public Response isAuthenticated() {
        return Response.ok().build();
    }

    @GET
    @PermitAll
    @Consumes(MediaType.WILDCARD)
    public Response auth(
            @NotEmpty @QueryParam("code") String code,
            @NotNull @QueryParam("state") SocialNetworkParam socialNetwork,
            @Context HttpServletRequest request) throws MalformedURLException, VkSideError, URISyntaxException {
        if(socialNetwork.getValue() == SocialNetwork.Other)
            throw new WebApplicationException("sn must be vk or facebook", Response.Status.BAD_REQUEST);
        String uri = request.getRequestURL().toString();
        if(sessionSecret == null)
            sessionSecret = AuthController.generateSessionSecret();

        NewCookie snIdCookie = new NewCookie(AuthController.USER_ID_COOKIE_NAME,
                authController.auth(code, uri, sessionSecret, socialNetwork.getValue()).toString(), "/", null,
                null, 2629744, false); // Valid for a month
        NewCookie sessionIdCookie = new NewCookie(AuthController.SESSION_COOKIE_NAME, sessionSecret, "/", null,
                null, 2629744, false); // Valid for a month

        return Response.temporaryRedirect(new URI("/client"))
                .cookie(snIdCookie)
                .cookie(sessionIdCookie)
                .build();

    }
}
