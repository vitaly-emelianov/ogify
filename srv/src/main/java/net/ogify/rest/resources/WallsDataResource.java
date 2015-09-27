package net.ogify.rest.resources;

import net.ogify.database.UserController;
import net.ogify.engine.secure.AuthController;
import net.ogify.engine.vkapi.elements.wall.WallPost;
import net.ogify.engine.vkapi.exceptions.VkSideError;
import net.ogify.engine.walls.WallPostsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
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

    @Autowired
    WallPostsService wallPostsService;

    @GET
    public List<WallPost> getAllPosts(@QueryParam("maxCount") @DefaultValue("1000") int maxCount) throws VkSideError {
        return wallPostsService.getAllPosts(maxCount);
    }

    @GET
    @Path("/my")
    public Set<WallPost> getMyPosts(@CookieParam(AuthController.USER_ID_COOKIE_NAME) Long userId) throws VkSideError {
        return wallPostsService.getWallPostsOfUser(userController.getUserById(userId));
    }

}
