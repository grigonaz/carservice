package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.RepairItemDao;
import cz.cvut.kbss.ear.carservice.model.RepairItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RepairItemService {

    private final RepairItemDao repairItemDao;

    @Autowired
    public RepairItemService(RepairItemDao repairItemDao) {
        this.repairItemDao = repairItemDao;
    }

    @Transactional(readOnly = true)
    public RepairItem findById(Integer id){
        return repairItemDao.find(id);
    }
}
