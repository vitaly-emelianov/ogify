package net.ogify.rest.resources;

import net.ogify.database.OrderController;
import net.ogify.database.entities.Order;
import net.ogify.database.entities.OrderItem;
import net.ogify.engine.order.OrderProcessor;
import net.ogify.engine.secure.AuthController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Class represents
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    /**
     * Field storing the vk id of the user if the user not authorized it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = AuthController.USER_ID_COOKIE_NAME)
    private Long userId;

    @Autowired
    OrderController orderController;

    @Autowired
    OrderProcessor orderProcessor;

    @GET
    @Path("/near")
    public Set<Order> getOrdersNear(@NotNull @QueryParam("latitude") Double latitude,
                                    @NotNull @QueryParam("longitude") Double longitude) throws ExecutionException {
        return orderProcessor.getNearestOrders(latitude, longitude, userId);
    }

    /**
     * Return, not more then specified, users orders, where user is owner or executor.
     * @param firstResult the position of the first result to retrieve.
     * @param maxResults the maximum number of results to retrieve.
     * @return found users orders.
     */
    @GET
    public List<Order> getMyOrders(@QueryParam("offset") int firstResult,
                                   @QueryParam("firstResult") int maxResults) {
        return orderProcessor.getUsersOrders(userId, firstResult, maxResults);
    }

    @GET
    @Path(("/{orderId}"))
    public Order getOrder(@NotNull @PathParam("orderId") Long orderId) {
        return orderController.getOrderById(orderId);
    }

    @GET
    @Path("/{orderId}/items}")
    public List<OrderItem> getOrderItems(@NotNull @PathParam("orderId") Long orderId) {
        return orderController.getOrderById(orderId).getItems();
    }

    /**
     * Create new order.
     *
     * @param order order body.
     * @return id of created order.
     */
    @POST
    public Long createNewOrder(Order order) {
        return orderProcessor.createOrder(userId, order);
    }

    @POST
    @Path("/{orderId}/status")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void completeOrder(@PathParam("orderId") Long orderId,
                              @NotNull @FormParam("status") Order.OrderStatus status) {
        orderProcessor.changeOrderStatus(userId, orderId, status);
    }

    @POST
    @Path("/{orderId}/rate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void rateOrder(@PathParam("orderId") Long orderId, @NotNull @FormParam("rate") double rate,
                          @FormParam("comment") String comment) {
        orderProcessor.rateOrderParty(userId, orderId, rate, comment);
    }
}
