package net.ogify.rest.resources;

import net.ogify.database.entities.SocialNetwork;
import net.ogify.engine.secure.AuthController;
import net.ogify.engine.vkapi.VkAuth;
import net.ogify.engine.vkapi.exceptions.VkSideError;
import net.ogify.rest.elements.SNRequestUri;
import org.hibernate.validator.constraints.NotEmpty;

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
public class AuthResource {
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
                                      @QueryParam("sn") @NotNull SocialNetwork socialNetwork) {
        URI authRequestUri = uriInfo.getBaseUriBuilder()
                .path(AuthResource.class) // Add class path
                .path(AuthResource.class, "auth").build();

        SNRequestUri generatedUriResponse;
        switch(socialNetwork) {
            case Vk:
                generatedUriResponse = VkAuth.getClientAuthUri(authRequestUri);
                break;
            case FaceBook:
                generatedUriResponse = new SNRequestUri();
                break;
            default:
                generatedUriResponse = new SNRequestUri();

        }
        return generatedUriResponse;
    }

    @GET
    @PermitAll
    @Path("/do")
    public Response auth(
            @NotEmpty @QueryParam("code") String code,
            @QueryParam("sn") @NotNull SocialNetwork socialNetwork,
            @Context HttpServletRequest request) throws MalformedURLException, VkSideError, URISyntaxException {
        String uri = request.getRequestURL().toString();
        if(sessionSecret == null)
            sessionSecret = AuthController.generateSessionSecret();

        NewCookie vkIdCookie = new NewCookie(AuthController.USER_ID_COOKIE_NAME,
                AuthController.auth(code, uri, sessionSecret, socialNetwork).toString(), "/", null,
                null, 2629744, false); // Valid for a month
        NewCookie sessionIdCookie = new NewCookie(AuthController.SESSION_COOKIE_NAME, sessionSecret, "/", null,
                null, 2629744, false); // Valid for a month

        return Response.ok()
                .cookie(vkIdCookie)
                .cookie(sessionIdCookie)
                .build();

    }
}
