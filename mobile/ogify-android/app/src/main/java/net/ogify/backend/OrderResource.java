package net.ogify.backend;

import net.ogify.backend.entities.Order;
import net.ogify.backend.entities.OrderItem;
import net.ogify.backend.helper.Callback;
import net.ogify.backend.helper.NetworkHandler;

import java.util.List;

public class OrderResource {
    private String base = "http://ogify-miptsail.rhcloud.com//rest/orders";

    public void getOrders(Double latitude, Double longitude, Callback<List<Order>> callback) {
        NetworkHandler nh = NetworkHandler.getInstance();
        nh.readList(base + "?latitude=" + latitude + "&longitude" + longitude, Order[].class, callback);
    }

    public void getOrder(Long orderId) {
        NetworkHandler nh = NetworkHandler.getInstance();
        nh.readList(base + "/orderId/" + orderId, Order[].class, new Callback<List<Order>>() {
            @Override
            public void callback(List<Order> orders) {
                for (Order order : orders) {
                    System.out.println(order);
                }
            }
        });
    }

    public void createNewOrder(Order order) {
        NetworkHandler nh = NetworkHandler.getInstance();
        nh.write(base, Order.class, order, new Callback<Order>() {
            @Override
            public void callback(Order orders) {
            }
        });
    }

    public void getOrderItems(Long orderId) {
        NetworkHandler nh = NetworkHandler.getInstance();
        nh.readList(base + "/orderId/" + orderId + "/items", OrderItem[].class, new Callback<List<OrderItem>>() {
            @Override
            public void callback(List<OrderItem> orderItems) {
                for (OrderItem orderItem : orderItems) {
                    System.out.println(orderItem);
                }
            }
        });
    }


}
