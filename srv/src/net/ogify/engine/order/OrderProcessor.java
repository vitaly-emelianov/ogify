package net.ogify.engine.order;

import net.ogify.database.OrderController;
import net.ogify.database.UserController;
import net.ogify.database.entities.Order;
import net.ogify.database.entities.Order.OrderStatus;
import net.ogify.database.entities.OrderItem;
import net.ogify.database.entities.User;
import net.ogify.engine.friends.FriendProcessor;
import net.ogify.engine.secure.exceptions.ForbiddenException;

import javax.ws.rs.NotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by melges on 24.02.2015.
 */
public class OrderProcessor {
    /**
     * Method creates order on behalf  of the specified user.
     * @param userId users id on behalf order should be created
     * @param order order which should be created.
     */
    public static void createOrder(Long userId, Order order) {
        order.setId(null); // It is a new order, id must be null
        for(OrderItem item: order.getItems()) {
            item.setId(null); // It is a new item, id must be null
            item.setOrder(order); // Create relation between item and orders
        }

        // Set correct owner
        order.setOwner(UserController.getUserById(userId));
        OrderController.saveOrUpdate(order);
    }

    /**
     * Get order by id for specified user.
     * @param userId id of user who requests order
     * @param orderId id of requested order
     * @return requested order or null if there are no order with specified id, or user haven't access to them.
     */
    public static Order getOrderById(Long userId, Long orderId) throws ExecutionException {
        Set<Long> friends = FriendProcessor.getUserFriendsIds(userId);
        Set<Long> friendsOfFriends = FriendProcessor.getUserExtendedFriendsIds(userId);
        return OrderController.getOrderByIdFiltered(userId, orderId, friends, friendsOfFriends);
    }

    /**
     * Search for orders near (in square with) specified point.
     * @param latitude latitude of point.
     * @param longitude longitude of point.
     * @param userId id of user who make request.
     * @return visible for specified user orders.
     */
    public static Set<Order> getNearestOrders(Double latitude, Double longitude, Long userId)
            throws ExecutionException {
        Set<Long> friends = FriendProcessor.getUserFriendsIds(userId);
        Set<Long> friendsOfFriends = FriendProcessor.getUserExtendedFriendsIds(userId);
        return new HashSet<Order>(
                OrderController.getNearestOrdersFiltered(userId, friends, friendsOfFriends, latitude, longitude));
    }

    /**
     * Method change workflow status of order.
     * @param changerUserId who is changing status
     * @param orderId id of changed order
     * @param status status which order should have after change
     */
    public static void changeOrderStatus(Long changerUserId, Long orderId, OrderStatus status) {
        User changer = UserController.getUserById(changerUserId);
        assert changer != null;
        Order order = OrderController.getOrderById(orderId);


        if(order == null) // We can't work if order not founded
            throw new NotFoundException(String.format("Order with id %d is not presented on server", orderId));
        if(order.isUserOwner(changer) && order.isUserExecutor(changer)) // Check that we have access to order
            throw new ForbiddenException("You haven't right for change status of the order");
        if(order.isInFinalState()) // Check that order not in Completed or Canceled state
            throw new ForbiddenException("You can't change status of completed or canceled orders");

        if(order.isUserExecutor(changer)) { // Orders executor try to change status
            switch(status) { // Executor can complete, start execution or take order back
                case Running:
                case Completed:
                case New:
                    order.setStatus(status);
                    break;
                default:
                    throw new ForbiddenException(
                            String.format("Executor can't change order status from %s to %s state",
                                    order.getStatus().toString(), status.toString()));
            }
        } else if(order.getStatus() != OrderStatus.Running) { // Owner mustn't change status of running order
            switch(status) {
                case Canceled:
                    order.setStatus(status);
                    break;
                default:
                    throw new ForbiddenException(
                            String.format("Owner can't change order status from %s to %s state",
                                    order.getStatus().toString(), status.toString()));
            }
        }

        // And finally, if we don't have errors save order
        OrderController.saveOrUpdate(order);
    }
}
