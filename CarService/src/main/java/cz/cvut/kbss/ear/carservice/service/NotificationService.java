package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.NotificationDao;
import cz.cvut.kbss.ear.carservice.dao.UserDao;
import cz.cvut.kbss.ear.carservice.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationDao notDao;
    private final UserDao userDao;
    private final UserService userService;

    @Autowired
    public NotificationService(NotificationDao notDao, UserDao userDao, UserService userService) {
        this.notDao = notDao;
        this.userDao = userDao;
        this.userService = userService;
    }

    @Transactional
    public Notification createNotification(Repair repair) {
        Objects.requireNonNull(repair);
        Notification n = new Notification(repair);
        notDao.persist(n);
        return n;
    }

    @Transactional
    public Notification createNotification(Order order) {
        Objects.requireNonNull(order);
        Notification n = new Notification(order);
        notDao.persist(n);
        return n;
    }

    @Transactional(readOnly = true)
    public List<Notification> findAll() {
        User user = userService.getCurrentUser();
        return notDao.findAll().stream()
                .filter(not -> not.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    // For admin and employee only
    @Transactional(readOnly = true)
    public List<Notification> findAllByUserName(String username) {
        User user = userDao.findByUsername(username);
        return notDao.findAll().stream()
                .filter(not -> not.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    // Must match id of user in Notification and authorised user, or must have role admin or employee
    @Transactional(readOnly = true)
    public Notification find(Integer id) {
        return notDao.find(id);
    }
}
