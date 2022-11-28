package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.ItemDao;
import cz.cvut.kbss.ear.carservice.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemDao itemDao;

    @Autowired
    public ItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Transactional(readOnly = true)
    public Item findById(Integer id){
        return itemDao.find(id);
    }
}
