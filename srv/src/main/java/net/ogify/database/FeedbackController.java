package net.ogify.database;

import net.ogify.database.entities.Feedback;
import org.apache.log4j.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by melges on 15.03.2015.
 */
public class FeedbackController {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OgifyDataSource");

    private final static Logger logger = Logger.getLogger(OrderController.class);

    public static void save(Feedback feedback) throws EntityExistsException {
        EntityManager em = emf.createEntityManager();
        try {

            em.getTransaction().begin();
            em.persist(feedback);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
