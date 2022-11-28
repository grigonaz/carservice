package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
public class CartServiceTest {

    //Functionality of cart was taken mostly from e-shop, besides function moving items from one cart to another
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CartService sut;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    private Cart cart1;

    private User user1;

    private Product product;

    @Before
    public void setUp() {
        user1 = new User();
        user1.setUsername("TestUser");
        user1.setFirstName("TestName");
        user1.setLastName("TestLastName");
        user1.setPassword("testPassword");
        user1.setRole(Role.CLIENT);
        user1.setMoney(100d);
        user1.setId(1);


        product = new Product();
        product.setName("test product");
        product.setAmount(5);
        product.setId(1);


        cart1 = new Cart();
        cart1.setOwner(user1);
        cart1.setId(1);
        user1.setCart(cart1);

    }

    @Test
    public void addItemAddsItemIntoCart() {
        final CartItem toAdd = new CartItem();
        toAdd.setProduct(product);
        toAdd.setAmount(1);
        toAdd.setId(1);
        Mockito.when(productService.findById(product.getId())).thenReturn(product);
        sut.addItem(cart1, toAdd);

        final Cart result = em.find(Cart.class, cart1.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(toAdd.getAmount(), result.getItems().get(0).getAmount());
        assertEquals(toAdd.getProduct().getId(), result.getItems().get(0).getProduct().getId());
    }

    @Test
    public void addItemUpdatesProductAmountBySubtractingAmountSpecifiedInItem() {
        int amount = product.getAmount();
        Mockito.when(productService.findById(product.getId())).thenReturn(product);
        final CartItem toAdd = new CartItem();
        toAdd.setProduct(product);
        final int toAddAmount = 1;
        toAdd.setAmount(toAddAmount);
        sut.addItem(cart1, toAdd);

        final Product result = em.find(Product.class, product.getId());
        assertEquals(amount - toAddAmount, result.getAmount().intValue());
    }

    @Test
    public void moveItemFromOneCartToAnotherTest() {
        User user2 = new User();
        Cart cart2 = new Cart();
        cart2.setOwner(user2);
        user2.setUsername("TestUser");
        user2.setFirstName("TestName");
        user2.setLastName("TestLastName");
        user2.setPassword("testPassword");
        user2.setRole(Role.CLIENT);
        user2.setMoney(100d);
        user2.setCart(cart2);
        em.persist(user2);
        em.persist(cart2);

        for (int i = 0; i < 10; i++) {
            Product pushProduct = new Product();
            pushProduct.setName("test product" + i);
            pushProduct.setAmount(5);
            em.persist(pushProduct);

            final CartItem toAdd = new CartItem();
            toAdd.setId(i + 10);
            toAdd.setProduct(pushProduct);
            final int toAddAmount = 1;
            toAdd.setAmount(toAddAmount);
            Mockito.when(productService.findById(pushProduct.getId())).thenReturn(pushProduct);
            sut.addItem(cart1, toAdd);
        }

        sut.moveItemsFromOneCartToAnother(cart1, cart2);

        assertEquals(0, cart1.getItems().size());
        assertEquals(10, cart2.getItems().size());
    }
}