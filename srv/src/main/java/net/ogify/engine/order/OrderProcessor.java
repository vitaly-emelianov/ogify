package net.ogify.engine.order;

import net.ogify.database.FeedbackController;
import net.ogify.database.OrderController;
import net.ogify.database.UserController;
import net.ogify.database.entities.Feedback;
import net.ogify.database.entities.Order;
import net.ogify.database.entities.Order.OrderStatus;
import net.ogify.database.entities.OrderItem;
import net.ogify.database.entities.User;
import net.ogify.engine.friends.FriendService;
import net.ogify.engine.secure.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by melges on 24.02.2015.
 */
@Service
public class OrderProcessor {
    @Autowired
    OrderController orderController;

    @Autowired
    UserController userController;

    @Autowired
    FeedbackController feedbackController;

    @Autowired
    FriendService friendService;

    /**
     * Method creates order on behalf  of the specified user.
     * @param userId users id on behalf order should be created
     * @param order order which should be created.
     *
     * @return created order.
     */
    public Order createOrder(Long userId, Order order) {
        order.setId(null); // It is a new order, id must be null
        order.makeCreatedNow(); // It was created just now
        for(OrderItem item: order.getItems()) {
            item.setId(null); // It is a new item, id must be null
            item.setOrder(order); // Create relation between item and orders
        }

        // Set correct owner
        order.setOwner(userController.getUserById(userId));
        orderController.save(order);

        return order;
    }

    /**
     * Get order by id for specified user.
     * @param userId id of user who requests order
     * @param orderId id of requested order
     * @return requested order or null if there are no order with specified id, or user haven't access to them.
     * @throws ExecutionException on any exception thrown while attempting to get results.
     */
    public Order getOrderById(Long userId, Long orderId) throws ExecutionException {
        Set<Long> friends = friendService.getUserFriendsIds(userId);
        Set<Long> friendsOfFriends = friendService.getUserExtendedFriendsIds(userId);
        return orderController.getOrderByIdFiltered(userId, orderId, friends, friendsOfFriends);
    }

    /**
     * Search for orders near (in square with) specified point.
     * @param latitude latitude of point.
     * @param longitude longitude of point.
     * @param userId id of user who make request.
     * @return visible for specified user orders.
     * @throws ExecutionException on any exception thrown while attempting to get results.
     */
    public Set<Order> getNearestOrders(Double latitude, Double longitude, Long userId)
            throws ExecutionException {
        Set<Long> friends = friendService.getUserFriendsIds(userId);
        Set<Long> friendsOfFriends = friendService.getUserExtendedFriendsIds(userId);
        return new HashSet<>(
                orderController.getNearestOrdersFiltered(userId, friends, friendsOfFriends, latitude, longitude));
    }

    /**
     * Method change workflow status of order.
     * @param changerUserId who is changing status
     * @param orderId id of changed order
     * @param status status which order should have after change
     */
    public void changeOrderStatus(Long changerUserId, Long orderId, OrderStatus status) {
        User changer = userController.getUserById(changerUserId);
        assert changer != null;
        Order order = orderController.getOrderById(orderId);


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
        orderController.saveOrUpdate(order);
    }

    /**
     *
     * @param userId id of user which orders should be retrieved.
     * @param firstResult the position of the first result to retrieve.
     * @param maxResults the maximum number of results to retrieve.
     * @return users orders.
     */
    public List<Order> getUsersOrders(Long userId, int firstResult, int maxResults) {
        return orderController.getUsersOrders(userId, firstResult, maxResults);
    }

    /**
     * Methods rate second member of order execution.
     * @param userId who rate.
     * @param orderId related order.
     * @param rate how he's rates.
     * @param comment additional comment for rate.
     */
    public void rateOrderParty(Long userId, Long orderId, double rate, String comment) {
        if(rate > 5 || rate < 0)
            throw new BadRequestException("Rate must be in [0, 5] range");

        User userWho = userController.getUserById(userId);
        Order relatedOrder = orderController.getUsersOrder(userId, orderId);

        // Check all conditions for rate order execution
        if(relatedOrder == null) // Check that order with specified id is presented
            throw new NotFoundException(String.format("Order with id %d not found", orderId));
        if(relatedOrder.getStatus() != OrderStatus.Completed) // Check that order is completed
            throw new WebApplicationException("You can't rate user while order isn't completed",
                    Response.Status.FORBIDDEN);
        if(orderController.isOrderRatedBy(relatedOrder, userWho)) // Check that user didn't rate order already
            throw new WebApplicationException("You can't rate order again", Response.Status.GONE);


        Feedback feedback;
        if(userWho.equals(relatedOrder.getExecutor())) {
            // Executor rates owner
            User userWhom = relatedOrder.getOwner();
            feedback = new Feedback(comment, userWho, userWhom, relatedOrder, rate);
        } else {
            // Owner rates executor
            User userWhom = relatedOrder.getExecutor();
            feedback = new Feedback(comment, userWho, userWhom, relatedOrder, rate);
        }

        feedbackController.save(feedback);
    }
}
