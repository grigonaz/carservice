package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.CartDao;
import cz.cvut.kbss.ear.carservice.dao.OrderDao;
import cz.cvut.kbss.ear.carservice.dao.UserDao;
import cz.cvut.kbss.ear.carservice.exceptions.EmptyCartException;
import cz.cvut.kbss.ear.carservice.exceptions.LackOfFundsException;
import cz.cvut.kbss.ear.carservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Objects;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final UserService userService;
    private final OrderItemService orderItemService;
    private final UserDao userDao;

    @Autowired
    public OrderService(OrderDao orderDao, CartDao cartDao, UserService userService, UserDao userDao, OrderItemService orderItemService, UserDao userDao1) {
        this.orderDao = orderDao;
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.userDao = userDao1;
    }

    @Transactional
    public Order createOrder(Cart cart) {
        Objects.requireNonNull(cart);
        Objects.requireNonNull(cart.getOwner());

        if (cart.getItems().size() <= 0) {
            throw new EmptyCartException("Attempt to create the Order with the empty cart");
        }

        int totalPrice = 0;

        for (CartItem c : cart.getItems()) {
            totalPrice += c.getProduct().getPrice() * c.getAmount();
        }

        if (cart.getOwner().getMoney() < totalPrice) {
            throw new LackOfFundsException("Attempt to create the order with the lack of funds on the balance");
        }

        Order order = new Order(cart);

        if (cart.getOwner() == null) {
            order.setClient(generateUser());
        }

        cart.getOwner().setMoney(cart.getOwner().getMoney() - totalPrice);

        orderDao.persist(order);

        Notification orderNotification = new Notification(order);
        cart.getOwner().getNotifications().add(orderNotification);

        changeStatus(order, OrderInfo.ORDERED);

        cart.getItems().clear();
        return order;
    }

    @Transactional
    public User generateUser() {
        User user = new User("User", "Order", "user", "password");
        userService.persist(user);

        return user;
    }

    @Transactional
    public void changeStatus(Order order, OrderInfo status) {
        order.setInfo(status);
        orderDao.update(order);
        Notification orderNotification = new Notification(order);
        order.getClient().getNotifications().add(orderNotification);
        userDao.update(order.getClient());
    }

    @Transactional
    public void persist(Order order){
        orderDao.persist(order);
    }
    @Transactional(readOnly = true)
    public Order findById(Integer id) {
        return orderDao.find(id);
    }

    @PostFilter("hasRole('ROLE_ADMIN') or (filterObject.customer.username  == principal.username)")
    public List<Order> findAll(){
        return orderDao.findAll();
    }
}
