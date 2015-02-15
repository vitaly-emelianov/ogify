package net.ogify.database;

import net.ogify.database.entities.User;

import javax.persistence.*;
import java.util.List;

/**
 * Created by melges.morgen on 14.02.15.
 */
public class UserController {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OgifyDataSource");

    public static User getUserById(Long userId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.getById", User.class);
            query.setParameter("id", userId);
            List<User> resultList = query.getResultList();

            if(resultList.size() == 1)
                return resultList.get(0);
            if(resultList.size() == 0)
                return null;

            throw new NonUniqueResultException("We receive more then one user with specified id, it mustn't happened");
        } finally {
            em.close();
        }
    }

    public static User getUserByFbId(Long userId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.getUserByFbId", User.class);
            query.setParameter("fbId", userId);
            List<User> resultList = query.getResultList();

            if(resultList.size() == 1)
                return resultList.get(0);
            if(resultList.size() == 0)
                return null;

            throw new NonUniqueResultException("We receive more then one user with specified id, it mustn't happened");
        } finally {
            em.close();
        }
    }

    public static User getUserByVkId(Long userId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.getUserByVkId", User.class);
            query.setParameter("vkId", userId);
            List<User> resultList = query.getResultList();

            if(resultList.size() == 1)
                return resultList.get(0);
            if(resultList.size() == 0)
                return null;

            throw new NonUniqueResultException("We receive more then one user with specified id, it mustn't happened");
        } finally {
            em.close();
        }
    }

    public static void saveOrUpdate(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (user.getId() == null)
                em.persist(user);
            else
                em.merge(user);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static User getUserByIdAndSession(Long userId, String sessionSecret) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.getByIdAndSession", User.class);
            query.setParameter("userId", userId);
            query.setParameter("sessionSecret", sessionSecret);
            List<User> resultList = query.getResultList();

            if(resultList.size() == 1)
                return resultList.get(0);
            if(resultList.size() == 0)
                return null;

            throw new NonUniqueResultException("We receive more then one user with specified id, it mustn't happened");
        } finally {
            em.close();
        }
    }
}
