package net.ogify.rest.resources;

import com.qmino.miredot.annotations.ReturnType;
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
    @Path("/getRequestUri")
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
     * @title Authenticate client
     *
     * Authenticate client redirected from social network. This is endpoint (redirect url should point to this method)
     * for OAuth2 protocol.
     *
     * @param code secret code returned by social network.
     * @param socialNetwork should show which social network used for authentication.
     * @param request request from context.
     * @return response which sets cookie and redirect client to client application.
     * @throws VkSideError if there error returned from vk on authentication process.
     * @throws URISyntaxException on error in producing redirect URI
     */
    @GET
    @PermitAll
    @Consumes(MediaType.WILDCARD)
    @ReturnType("java.lang.Void")
    public Response auth(
            @NotEmpty @QueryParam("code") String code,
            @NotNull @QueryParam("state") SocialNetworkParam socialNetwork,
            @Context HttpServletRequest request) throws VkSideError, URISyntaxException {
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

        return Response.seeOther(new URI("/client"))
                .cookie(snIdCookie)
                .cookie(sessionIdCookie)
                .build();

    }
}
