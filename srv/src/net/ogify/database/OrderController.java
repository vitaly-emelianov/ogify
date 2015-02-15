package net.ogify.database;

import net.ogify.database.entities.Order;
import net.ogify.database.entities.OrderItem;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by melges.morgen on 15.02.15.
 */
public class OrderController {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OgifyDataSource");

    private final static Logger logger = Logger.getLogger(OrderController.class);

    public static void createOrder(Order order) {
        for(OrderItem item: order.getItems()) {
            item.setOrder(order);
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
        } catch(RuntimeException e) {
            em.getTransaction().rollback();
            logger.error("Error on order creation!", e);
            throw e;
        } finally {
            em.close();
        }
    }
}
