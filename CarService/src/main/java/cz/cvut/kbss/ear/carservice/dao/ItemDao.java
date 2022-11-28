package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public class ItemDao extends BaseDao<Item> {
    protected ItemDao() {
        super(Item.class);
    }
}

