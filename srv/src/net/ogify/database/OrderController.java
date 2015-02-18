package net.ogify.database;

import net.ogify.database.entities.Order;
import net.ogify.database.entities.OrderItem;
import net.ogify.database.entities.User;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by melges.morgen on 15.02.15.
 */
public class OrderController {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OgifyDataSource");

    private final static Logger logger = Logger.getLogger(OrderController.class);

    public static void createOrder(Long userId, Order order) {
        order.setId(null);
        for(OrderItem item: order.getItems()) {
            item.setId(null);
            item.setOrder(order);
        }

        EntityManager em = emf.createEntityManager();
        try {
            User creator = em.find(User.class, userId);
            order.setOwner(creator);
            em.getTransaction().begin();
            em.merge(order);
            em.getTransaction().commit();
        } catch(RuntimeException e) {
            em.getTransaction().rollback();
            logger.error("Error on order creation!", e);
            throw e;
        } finally {
            em.close();
        }
    }

    public static Order getOrderById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Order.class, id);
        } finally {
            em.close();
        }
    }

    public static List<Order> getNearest(Double latitude, Double longitude) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Order> query = em.createNamedQuery("Order.getNearestOrder", Order.class);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("orderStatus", Order.OrderStatus.New);
            query.setParameter("orderNamespace", Order.OrderNamespace.All);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
