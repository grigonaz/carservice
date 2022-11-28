package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao extends BaseDao <Order> {
    protected OrderDao() {
        super(Order.class);
    }
}
