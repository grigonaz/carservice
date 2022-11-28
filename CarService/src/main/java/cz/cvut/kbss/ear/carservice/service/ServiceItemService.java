package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.ServiceItemDao;
import cz.cvut.kbss.ear.carservice.model.ServiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceItemService {

    private final ServiceItemDao serviceItemDao;

    @Autowired
    public ServiceItemService(ServiceItemDao serviceItemDao) {
        this.serviceItemDao = serviceItemDao;
    }

    @Transactional(readOnly = true)
    public ServiceItem findById(Integer id){
        return serviceItemDao.find(id);
    }
}
