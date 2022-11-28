package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.exceptions.PersistenceException;
import cz.cvut.kbss.ear.carservice.model.Repair;
import cz.cvut.kbss.ear.carservice.model.RepairItem;
import cz.cvut.kbss.ear.carservice.model.ServiceItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Repository
public class RepairItemDao{

    @PersistenceContext
    private EntityManager em;

    public RepairItem find(Integer id) {
        Objects.requireNonNull(id);

        return em.find(RepairItem.class, id);
    }

    public List<RepairItem> findAll() {
        return em.createQuery("select r from RepairItem r where r.amount > 0", RepairItem.class).getResultList();
    }

    public void persist(RepairItem repairItem) {
        Objects.requireNonNull(repairItem);

        try {
            em.persist(repairItem);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}

