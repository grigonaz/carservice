package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.ItemDao;
import cz.cvut.kbss.ear.carservice.model.Item;
import cz.cvut.kbss.ear.carservice.model.OrderItem;
import cz.cvut.kbss.ear.carservice.model.OrderItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class OrderItemService {

    private final ItemDao itemDao;

    @Autowired
    public OrderItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Transactional
    public void changeStatus(OrderItem item, OrderItemInfo status){
        Objects.requireNonNull(item);
        Objects.requireNonNull(status);

        item.setInfo(status);
        update(item);
    }

    @Transactional(readOnly = true)
    public Item find(Integer id) {
        return itemDao.find(id);
    }

    @Transactional
    public void update(OrderItem item){
        itemDao.update(item);
    }
}
