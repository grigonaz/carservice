package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class CartDao extends BaseDao<Cart> {
    protected CartDao() {
        super(Cart.class);
    }
}
