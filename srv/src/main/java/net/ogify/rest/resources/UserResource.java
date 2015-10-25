package net.ogify.rest.resources;

import net.ogify.database.UserController;
import net.ogify.database.entities.Order;
import net.ogify.database.entities.User;
import net.ogify.engine.friends.FriendService;
import net.ogify.engine.order.OrderProcessor;
import net.ogify.engine.secure.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Class represent api for work with users and theirs resources.
 *
 * @author Morgen Matvey
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class UserResource {
    @Autowired
    private UserController userController;

    @Autowired
    private FriendService friendService;

    @Autowired
    private OrderProcessor orderProcessor;

    /**
     * Field storing the id of the user if the user not authorized it is null.
     * Can be null only in permitted for all methods.
     */
    @CookieParam(AuthController.USER_ID_COOKIE_NAME)
    private Long currentUserId;

    @GET
    @Path("/{id}")
    public User getUserById(@PathParam("id") Long id) {
        return userController.getUserById(id);
    }

    @GET
    public User getCurrentUser() {
        return userController.getUserById(currentUserId);
    }

    @GET
    @Path("/{id}/friends")
    public Set<Long> getUserFriends(@PathParam("id") Long userId) throws ExecutionException {
        return friendService.getUserFriendsIds(userId);
    }

    @GET
    @Path("/{id}/extendedFriends")
    public Set<Long> getUserExtendedFriends(@PathParam("id") Long userId) throws ExecutionException {
        return friendService.getUserExtendedFriendsIds(userId);
    }

    /**
     * Method returns orders which user is running, ordered by "expire in" field.
     *
     * @summary Returns orders which user is running.
     * @param userId id of user which orders will be returned.
     * @return list of orders is running by specified user.
     */
    @GET
    @Path("/{id}/executing")
    public List<Order> getExecutingByUser(@PathParam("id") Long userId) {
        return orderProcessor.getOrdersByExecutor(userId, currentUserId, Order.OrderStatus.Running);
    }

    /**
     * Method returns orders which user has completed, ordered by "expire in" field.
     *
     * @summary Returns orders which user has completed.
     * @param userId id of user which orders will be returned.
     * @return list of orders completed by specified user.
     */
    @GET
    @Path("/{id}/executed")
    public List<Order> getExecutedByUser(@PathParam("id") Long userId) {
        return orderProcessor.getOrdersByExecutor(userId, currentUserId, Order.OrderStatus.Completed);
    }

    /**
     * Method returns orders which user has failed, ordered by "expire in" field.
     *
     * @summary Returns orders which user has failed.
     * @param userId id of user which orders will be returned.
     * @return list of orders failed by specified user.
     */
    @GET
    @Path("/{id}/failed")
    public List<Order> getFailedByUser(@PathParam("id") Long userId) {
        return orderProcessor.getOrdersByExecutor(userId, currentUserId, Order.OrderStatus.Failed);
    }

    /**
     * Return orders created by user. Orders will be ordered by "expire in" field.
     *
     * @summary Return orders created by user.
     * @param userId id of user which orders will be returned.
     * @param firstResult number of first result from the first of all orders.
     * @param maxResults number of orders in result list.
     * @return list of orders created by specified user.
     */
    @GET
    @Path("/{id}/created")
    public List<Order> getCreatedByUserOrders(
            @PathParam("id") Long userId,
            @NotNull @QueryParam("firstParam") int firstResult,
            @NotNull @QueryParam("maxResults") int maxResults) {
        return orderProcessor.getUsersOrders(userId, currentUserId, firstResult, maxResults);
    }

    /**
     * Return orders which wasn't rated by user.
     *
     * @summary Unrated users orders.
     * @param userId id of user which unrated orders will be returned.
     * @return ids of unrated orders.
     */
    @GET
    @Path("/{id}/unrated")
    public List<Long> getUnratedOrders(@NotNull @PathParam("id") Long userId) {
        return orderProcessor.getUnratedOrders(currentUserId, userId);
    }
}
