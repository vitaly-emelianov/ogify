package net.ogify.rest.resources;

import net.ogify.database.UserController;
import net.ogify.database.entities.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @GET
    @Path("{id}")
    public User getUserById(@PathParam("id") Long id) {
        return UserController.getUserById(id);
    }
}
