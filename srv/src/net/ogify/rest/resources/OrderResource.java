package net.ogify.rest.resources;

import net.ogify.database.OrderController;
import net.ogify.database.entities.Order;
import net.ogify.database.entities.OrderItem;
import net.ogify.engine.secure.AuthController;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melges.morgen on 15.02.15.
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
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

    @GET
    public List<Order> getOrders(@NotNull @QueryParam("latitude") Double latitude,
                          @NotNull @QueryParam("longitude") Double longitude) {
        return new ArrayList<Order>();
    }

    @GET
    @Path(("{orderId}"))
    public Order getOrder(@NotNull @PathParam("orderId") Long orderId) {
        return OrderController.getOrderById(orderId);
    }

    @POST
    public void createNewOrder(Order order) {
        OrderController.createOrder(order);
    }

    @GET
    @Path("{orderId}/items}")
    public List<OrderItem> getOrderItems(@NotNull @PathParam("orderId") Long orderId) {
        return new ArrayList<OrderItem>();
    }


}
