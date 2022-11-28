package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.RepairDao;
import cz.cvut.kbss.ear.carservice.dto.CarDto;
import cz.cvut.kbss.ear.carservice.exceptions.EmptyCartException;
import cz.cvut.kbss.ear.carservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepairService {

    private final RepairDao repairDao;
    private final UserService userService;

    @Autowired
    public RepairService(RepairDao repairDao, UserService userService) {
        this.repairDao = repairDao;
        this.userService = userService;
    }

    @Transactional
    public Repair createRepair(RepairCart repairCart, CarDto carDto) {
        Objects.requireNonNull(repairCart);
        Objects.requireNonNull(repairCart.getOwner());

        if (repairCart.getItems().size() <= 0) {
            throw new EmptyCartException("Attempt to create Repair with empty cart");
        }

        Repair newRepair = new Repair(repairCart,carDto);

        if (repairCart.getOwner() == null) {
            newRepair.setClient(generateUser());
        }

        repairDao.persist(newRepair);

        changeStatus(newRepair, RepairStatus.WAITING);

        repairCart.getItems().clear();
        return newRepair;
    }

    @Transactional
    public User generateUser() {
        User user = new User("User", "Order", "user", "password");
        userService.persist(user);

        return user;
    }

    @Transactional
    public void changeStatus(Repair repair, RepairStatus rs) {
        repair.setRepairStatus(rs);
        Notification orderNotification = new Notification(repair);
        repair.getClient().getNotifications().add(orderNotification);
        repairDao.update(repair);
        userService.update(repair.getClient());
    }

    @Transactional
    public List<User> getEmployeesWorkedOnRepair(Repair repair) {
        List<User> empls;
        if (repair.getWorks() == null) {
            repair.setWorks(new ArrayList<>());
            return new ArrayList<>();
        }

        List<Work> works;
        works = repair.getWorks();
        empls = works.stream().map(Work::getEmployee).distinct().collect(Collectors.toList());

        return empls;
    }

    @Transactional(readOnly = true)
    public void findAllNotFinished() {
        repairDao.findAllNotFinished();
    }

    @Transactional
    public Repair findById(Integer id) {
        return repairDao.find(id);
    }


    @PostFilter("hasRole('ROLE_ADMIN')")
    public List<Repair> findAll() {
        User user = userService.getCurrentUser();
        List<Repair> repairs = new ArrayList<>(repairDao.findAll());

        if (user.getRole() == Role.ADMIN) {
            return repairs;
        }

        return repairs.stream().filter(repair -> repair.getClient() == user).collect(Collectors.toList());
    }

    @Transactional
    public void update(Repair repair) {
        repairDao.update(repair);
    }
}
