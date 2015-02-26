package net.ogify.engine.order;

import net.ogify.database.OrderController;
import net.ogify.database.UserController;
import net.ogify.database.entities.Order;
import net.ogify.database.entities.OrderItem;
import net.ogify.database.entities.User;
import net.ogify.engine.friends.FriendProcessor;

import java.util.HashSet;
import java.util.List;
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
     * Get order by id for specified user. R
     * @param userId id of user who requests order
     * @param orderId id of requested order
     * @return requested order or null if there are no order with specified id, or user haven't access to them.
     */
    public static Order getOrderById(Long userId, Long orderId) throws ExecutionException {
        Set<Long> friends = FriendProcessor.getUserFriendsIds(userId);
        Set<Long> friendsOfFriends = new HashSet<>();
        return OrderController.getOrderById(userId, orderId, friends, friendsOfFriends);
    }
}
