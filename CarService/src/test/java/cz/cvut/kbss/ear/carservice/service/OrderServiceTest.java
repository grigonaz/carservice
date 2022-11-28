package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.environment.Generator;
import cz.cvut.kbss.ear.carservice.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderService sut;

    @Test
    public void createOrderCreatesOrder() {
        Cart cart = generateNotEmptyCart();
        Order order = sut.createOrder(cart);

        assertEquals(cart.getItems().size(), 0);
    }

    @Test
    public void createOrderThenCartHasNoOwnerThrowsNullPointer() {
        Cart cart = generateNotEmptyCart();
        cart.setOwner(null);
        assertThrows(NullPointerException.class, () -> sut.createOrder(cart));
    }

    @Test
    public void orderCreationSetsCreateDate() {
        Cart cart = generateNotEmptyCart();
        Order order = sut.createOrder(cart);

        assertNotNull(order.getCreated());
    }

    @Test
    public void createOrderFromSpecifiedCart() {
        final Cart cart = generateNotEmptyCart();
        final List<CartItem> items = new ArrayList<>(cart.getItems());

        final Order order = sut.createOrder(cart);
        assertNotNull(order);

        final Order result = em.find(Order.class, order.getId());
        assertEquals(items.size(), result.getItems().size());

        for (int i = 0; i < items.size(); i++) {
            final CartItem cartItem = items.get(i);
            final OrderItem orderResult = order.getItems().get(i);

            assertEquals(cartItem.getAmount(), orderResult.getAmount());
            assertEquals(cartItem.getProduct().getId(), orderResult.getProduct().getId());
        }
    }

    private Cart generateNotEmptyCart() {
        final Cart cart = new Cart();
        cart.setOwner(generateUser());

        em.persist(cart.getOwner());
        em.persist(cart);

        for (int i = 0; i < 10; i++) {
            final Product p = generateProduct();
            p.setAmount(10);
            em.persist(p);

            final CartItem cartItem = new CartItem();
            cartItem.setProduct(p);
            cartItem.setAmount(i);
            cart.addItem(cartItem);
            em.persist(cartItem);
        }

        return cart;
    }

    private User generateUser() {
        User user = new User("Test", "Test", "test" + Generator.randomInt(), "test123");
        user.setMoney(100000000000d);
        em.persist(user);
        return user;
    }

    private Product generateProduct() {
        Product product = new Product("testProd" + Generator.randomInt(), Generator.randomInt(), 10.0);
        em.persist(product);
        return product;
    }

    @Test
    public void changeOrderStatus() {
        Cart cart = generateNotEmptyCart();
        Order order = sut.createOrder(cart);

        order.setInfo(OrderInfo.ORDERED);
        sut.changeStatus(order, OrderInfo.ONTHEWAY);

        assertEquals(OrderInfo.ONTHEWAY, order.getInfo());
    }

    @Test
    public void findOrderById() {
        Cart cart = generateNotEmptyCart();
        Order order = sut.createOrder(cart);

        Order result = sut.findById(order.getId());

        assertEquals(order, result);
    }
}
