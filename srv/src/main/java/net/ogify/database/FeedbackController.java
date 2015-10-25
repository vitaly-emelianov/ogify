package net.ogify.database;

import net.ogify.database.entities.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by melges on 15.03.2015.
 */
@Component
public class FeedbackController {
    @Autowired
    EntityManagerService entityManagerService;

    public void save(Feedback feedback) throws EntityExistsException {
        EntityManager em = entityManagerService.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(feedback);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Long getUsersRateForOrder(Long userId, Long orderId) {
        EntityManager em = entityManagerService.createEntityManager();
        try {
            TypedQuery<Long> query = em.createNamedQuery("Feedback.getFeedback", Long.class);
            query.setParameter("whichOrderId", orderId);
            query.setParameter("whoRateId", userId);

            List<Long> resultList = query.getResultList();
            if(resultList.size() == 1)
                return resultList.get(0);
            if(resultList.size() == 0)
                return null;

            throw new NonUniqueResultException("We receive more then one order with specified id, it mustn't happened");
        } finally {
            em.close();
        }
    }
}
