package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.AutoServiceDao;
import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.model.AutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AutoServiceService {

    private final AutoServiceDao autoServiceDao;

    @Autowired
    public AutoServiceService(AutoServiceDao autoServiceDao) {
        this.autoServiceDao = autoServiceDao;
    }

    @Transactional
    public AutoService createAutoService(AutoService autoService) {
        String name = autoService.getName();
        Double price = autoService.getPrice();

        Objects.requireNonNull(name);
        Objects.requireNonNull(price);

        if (existService(name)) {
            throw new InvalidDataException("Service with name" + name + "already exist");
        }

        if (name.equals("") || price < 0) {
            throw new InvalidDataException("Attempt to create the service with this data: name: " +
                    name + " price: " + price);
        }

        autoServiceDao.persist(autoService);

        return autoService;
    }

    @Transactional
    public void update(AutoService autoService) {
        autoServiceDao.update(autoService);
    }

    @Transactional
    public void deactivate(AutoService autoService){
        autoService.setAvailable(false);
        update(autoService);
    }

    @Transactional(readOnly = true)
    public boolean existService(String username) {
        return autoServiceDao.findByName(username) != null;
    }

    @Transactional(readOnly = true)
    public AutoService findById(Integer id) {
        return autoServiceDao.find(id);
    }

    @Transactional(readOnly = true)
    public List<AutoService> findAll() {
        return autoServiceDao.findAll();
    }
}
