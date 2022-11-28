package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.AutoServiceDao;
import cz.cvut.kbss.ear.carservice.dao.RepairCartDao;
import cz.cvut.kbss.ear.carservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class RepairCartService {

    private final RepairCartDao repairCartDao;
    private final AutoServiceDao autoServiceDao;
    private final UserService userService;

    @Autowired
    public RepairCartService(RepairCartDao repairCartDao, AutoServiceDao autoServiceDao, UserService userService) {
        this.repairCartDao = repairCartDao;
        this.autoServiceDao = autoServiceDao;
        this.userService = userService;
    }

    @Transactional
    public void addItem(RepairCart repairCart, ServiceItem item) {
        Objects.requireNonNull(item);
        Objects.requireNonNull(repairCart);
        repairCart.addItem((ServiceItem) item);
        repairCartDao.update(repairCart);
    }

    @Transactional
    public void removeItem(RepairCart repairCart, ServiceItem item) {
        Objects.requireNonNull(item);
        Objects.requireNonNull(repairCart);

        repairCart.removeItem((ServiceItem) item);

        repairCartDao.update(repairCart);
    }

    @Transactional
    public RepairCart getCartByUserId(Integer id){
        return userService.findUser(id).getRepairCart();
    }

    @Transactional
    public void removeAll(RepairCart cart) {
        Objects.requireNonNull(cart);
        cart.setItems(new ArrayList<>());
        repairCartDao.update(cart);
    }
}
