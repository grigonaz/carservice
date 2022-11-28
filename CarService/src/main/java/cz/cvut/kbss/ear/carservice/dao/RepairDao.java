package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepairDao extends BaseDao<Repair> {

    protected RepairDao() {
        super(Repair.class);
    }

    public List<Repair> findAllNotFinished() {
      return em.createQuery("SELECT r FROM Repair r WHERE r.repairStatus <> 'STATUS_DONE'",Repair.class).getResultList();
    }
}
