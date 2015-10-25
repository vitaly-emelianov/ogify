package net.ogify.rest.elements;

import net.ogify.database.entities.Order;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * Created by melge on 25.10.2015.
 */
@XmlRootElement
public class OrdersWithSocialLinks {
    private List<Order> orders;

    private Map<Long, Order.OrderNamespace> socialLinks;

    public OrdersWithSocialLinks() {
    }

    public OrdersWithSocialLinks(List<Order> orders, Map<Long, Order.OrderNamespace> socialLinks) {
        this.orders = orders;
        this.socialLinks = socialLinks;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Map<Long, Order.OrderNamespace> getSocialLinks() {
        return socialLinks;
    }
}
