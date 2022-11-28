package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.AutoService;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class AutoServiceDao extends BaseDao<AutoService> {

    protected AutoServiceDao() {
        super(AutoService.class);
    }

    public AutoService findByName(String name) {
        try {
            return em.createNamedQuery("AutoService.findByName", AutoService.class).setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<AutoService> findAll() {
        try {
            return em.createNamedQuery("AutoService.findAllAvailable", AutoService.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
