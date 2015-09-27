package net.ogify.rest.resources;

import net.ogify.database.UserController;
import net.ogify.database.entities.User;
import net.ogify.engine.secure.AuthController;
import net.ogify.engine.vkapi.VkWall;
import net.ogify.engine.vkapi.elements.wall.WallPost;
import net.ogify.engine.vkapi.exceptions.VkSideError;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Class represents api for getting wall posts collected from vk
 */
@Path("/data/walls")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WallsDataResource {
    @Autowired
    UserController userController;

    @GET
    public Set<WallPost> getAllPosts(@CookieParam(AuthController.USER_ID_COOKIE_NAME) Long userId) throws VkSideError {
        User ourUser = userController.getUserById(userId);
        return VkWall.getPosts(ourUser.getVkId(), ourUser.getVkToken().getToken());
    }

}
