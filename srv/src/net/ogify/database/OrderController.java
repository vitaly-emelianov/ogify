package net.ogify.database;

import net.ogify.database.entities.Order;
import net.ogify.database.entities.OrderItem;
import net.ogify.database.entities.User;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by melges.morgen on 15.02.15.
 */
public class OrderController {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OgifyDataSource");

    private final static Logger logger = Logger.getLogger(OrderController.class);

    public static Order getOrderById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Order.class, id);
        } finally {
            em.close();
        }
    }

    public static List<Order> getNearestOrders(Double latitude, Double longitude) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Order> query = em.createNamedQuery("Order.getNearestOrder", Order.class);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static List<Order> getNearestOrdersFiltered(Long userId, Set<Long> userFriends, Set<Long> extendedFriends,
                                                 Double latitude, Double longitude) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Order> query = em.createNamedQuery("Order.getNearestOrdersFiltered", Order.class);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("userExtendedFriends", extendedFriends);
            query.setParameter("userFriends", userFriends);
            query.setParameter("userId", userId);

            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public static Order getOrderById(Long userId, Long orderId, Set<Long> friends, Set<Long> extendedFriends) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Order> query = em.createNamedQuery("Order.getOrderByIdFiltered", Order.class);
            query.setParameter("orderId", orderId);
            query.setParameter("userId", userId);
            query.setParameter("friends", friends);
            query.setParameter("extendedFriends", extendedFriends);

            List<Order> resultList = query.getResultList();
            if(resultList.size() == 1)
                return resultList.get(0);
            if(resultList.size() == 0)
                return null;

            throw new NonUniqueResultException("We receive more then one user with specified id, it mustn't happened");
        } finally {
            em.close();
        }
    }

    public static void saveOrUpdate(Order order) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(order);
            em.getTransaction().commit();
        } catch(RuntimeException e) {
            em.getTransaction().rollback();
            logger.error("Error on order save!", e);
            throw e;
        } finally {
            em.close();
        }
    }
}
