package cz.cvut.kbss.ear.carservice.process;

import cz.cvut.kbss.ear.carservice.dao.OrderDao;
import cz.cvut.kbss.ear.carservice.dao.UserDao;
import cz.cvut.kbss.ear.carservice.dto.RegisterDto;
import cz.cvut.kbss.ear.carservice.model.*;
import cz.cvut.kbss.ear.carservice.service.CartService;
import cz.cvut.kbss.ear.carservice.service.OrderItemService;
import cz.cvut.kbss.ear.carservice.service.OrderService;
import cz.cvut.kbss.ear.carservice.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderProcessTests {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

    @Autowired
    private OrderService sut;

    private User user;

    @Test
    public void fullBuyProcessTest() {
        RegisterDto userDto = new RegisterDto("testtest", "testtest", "testtest", "testtest");
        Mockito.when(userService.createNewUser(userDto)).thenCallRealMethod();
        user = userService.createNewUser(userDto);
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Product product1 = new Product("TestProduct1", 10, 100d);
        Product product2 = new Product("TestProduct2", 10, 200d);
        Product product3 = new Product("TestProduct3", 10, 300d);
        em.persist(product1);
        em.persist(product2);
        em.persist(product3);

        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();
        CartItem item3 = new CartItem();
        item1.setProduct(product1);
        item2.setProduct(product2);
        item3.setProduct(product3);

        item1.setAmount(1);
        item2.setAmount(2);
        item3.setAmount(3);

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);

        Cart cart = user.getCart();
        cartService.addItem(cart, item1);
        cartService.addItem(cart, item2);
        cartService.addItem(cart, item3);

        assertEquals(cart.getItems().size(), 3);
        cartService.removeItem(cart, item1);
        assertEquals(cart.getItems().size(), 2);
        orderService.createOrder(cart);


    }
}
