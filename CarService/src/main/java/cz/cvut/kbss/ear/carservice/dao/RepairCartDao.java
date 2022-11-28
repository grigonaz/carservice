package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.RepairCart;
import org.springframework.stereotype.Repository;

@Repository
public class RepairCartDao extends BaseDao<RepairCart> {
    protected RepairCartDao() {
        super(RepairCart.class);
    }
}
