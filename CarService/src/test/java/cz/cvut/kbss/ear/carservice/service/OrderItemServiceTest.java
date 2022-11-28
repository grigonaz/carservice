package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.CartDao;
import cz.cvut.kbss.ear.carservice.dao.ProductDao;
import cz.cvut.kbss.ear.carservice.dao.UserDao;
import cz.cvut.kbss.ear.carservice.dto.RegisterDto;
import cz.cvut.kbss.ear.carservice.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderItemServiceTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderItemService sut;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CartDao cartDao;

    @Test
    public void correctCreatingOrderItem() {
        Product product1 = new Product("TestProduct1", 10, 100d);
        Product product2 = new Product("TestProduct2", 10, 200d);
        Product product3 = new Product("TestProduct3", 10, 300d);
        productDao.persist(product1);
        productDao.persist(product2);
        productDao.persist(product3);


        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();
        CartItem item3 = new CartItem();
        item1.setProduct(product1);
        item2.setProduct(product2);
        item3.setProduct(product3);
        item1.setAmount(1);
        item2.setAmount(1);
        item3.setAmount(1);
        User user = new User();
        user.setId(1);
        user.setMoney(10000d);
        user.setNotifications(new ArrayList<>());
        Cart cart = new Cart();
        cart.setId(1);
        user.setCart(cart);
        cart.setOwner(user);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(item1);
        cartItems.add(item2);
        cartItems.add(item3);
        cart.setItems(cartItems);

        List<OrderItem> expect = cartItems.stream().map(OrderItem::new).collect(Collectors.toList());

       Order order = orderService.createOrder(cart);

        assertEquals(expect.get(0).getProduct().toString(), orderService.findById(order.getId()).getItems().get(0).getProduct().toString());
        assertEquals(expect.get(1).getProduct().toString(), orderService.findById(order.getId()).getItems().get(1).getProduct().toString());
        assertEquals(expect.get(2).getProduct().toString(), orderService.findById(order.getId()).getItems().get(2).getProduct().toString());
        assertSame(OrderItemInfo.ORDERED, orderService.findById(order.getId()).getItems().get(2).getInfo());
    }

    @Test
    public void correctChangingOfStatus() {
        Product product = new Product("TestProduct1", 10, 100d);
        OrderItem item1 = new OrderItem();
        item1.setAmount(10);
        item1.setInfo(OrderItemInfo.ORDERED);
        item1.setProduct(product);
        em.persist(item1);

        sut.changeStatus(item1, OrderItemInfo.ONTHEWAY);

        assertSame(OrderItemInfo.ONTHEWAY, item1.getInfo());
    }
}
