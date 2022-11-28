package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.User;
import cz.cvut.kbss.ear.carservice.model.Work;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WorkDao extends BaseDao<Work> {

    protected WorkDao() {
        super(Work.class);
    }

    public List<Work> findAll(User employee) {
        return em.createQuery("SELECT w from Work w where w.employee = :employee", Work.class).setParameter("employee", employee).getResultList();
    }
}
