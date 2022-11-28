package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.exceptions.PersistenceException;
import cz.cvut.kbss.ear.carservice.model.ServiceItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Repository
public class ServiceItemDao{

    @PersistenceContext
    private EntityManager em;

    public ServiceItem find(Integer id) {
        Objects.requireNonNull(id);

        return em.find(ServiceItem.class, id);
    }

    public List<ServiceItem> findAll() {
        return em.createQuery("select s from ServiceItem s where s.amount > 0", ServiceItem.class).getResultList();
    }

    public void persist(ServiceItem serviceItem) {
        Objects.requireNonNull(serviceItem);

        try {
            em.persist(serviceItem);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
