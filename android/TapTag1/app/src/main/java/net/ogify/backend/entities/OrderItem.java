package net.ogify.backend.entities;

public class OrderItem {
    Long id;
    Double expectedCost;
    String comment;
    Order order;

    public void setOrder(Order order) {
        this.order = order;
    }
}
