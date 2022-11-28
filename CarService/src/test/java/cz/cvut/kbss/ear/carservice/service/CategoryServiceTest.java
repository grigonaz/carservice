package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.exceptions.PermissionException;
import cz.cvut.kbss.ear.carservice.model.*;
import org.junit.Assert;
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
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CategoryServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CategoryService sut;

    @Test
    public void createCategoryThrowsInvalidDataExceptionIfCategoryAlreadyExists() {
        User user = new User("firstName", "lastName", "username", "password");
        user.setRole(Role.ADMIN);
        em.persist(user);
        Category category = new Category();
        category.setName("testCat");

        Category cat1 = sut.createCategory(category);

        Category category2 = new Category();
        category2.setName("testCat");

        assertThrows(InvalidDataException.class, () -> sut.createCategory(category2));
    }


    @Test
    public void createCategoryThrowsInvalidDataExceptionIfCategoryHasEmptyName() {
        User user = new User("firstName", "lastName", "username", "password");
        user.setRole(Role.ADMIN);
        em.persist(user);

        Category category = new Category();
        category.setName("");

        assertThrows(InvalidDataException.class, () -> sut.createCategory(category));
    }

    @Test
    public void addProductAddsProductsToCategory() {
        Product prod1 = new Product("myProd1", 1000, 10.0);
        Product prod2 = new Product("myProd2", 1000, 10.0);
        Category category = new Category("testCat");
        em.persist(prod1);
        em.persist(prod2);
        em.persist(category);

        sut.addProductToCategory(category, prod1);
        sut.addProductToCategory(category, prod2);

        Product result1 = em.find(Product.class, prod1.getId());
        Product result2 = em.find(Product.class, prod2.getId());

        assertTrue(result1.getCategories().stream().anyMatch(c -> c.getId().equals(category.getId())));
        assertTrue(result2.getCategories().stream().anyMatch(c -> c.getId().equals(category.getId())));
    }

    @Test
    public void addProductWithCategoryAddsProductsToCategory() {
        Product prod1 = new Product("myProd1", 1000, 10.0);
        Category category = new Category("Salam");

        prod1.setCategories(new ArrayList<>(Collections.singletonList(category)));

        em.persist(category);
        em.persist(prod1);

        sut.addProductToCategory(category, prod1);

        Product result = em.find(Product.class, prod1.getId());

        Assert.assertTrue(result.getCategories().contains(category));
    }



    @Test
    public void removeProductRemovesProductFromCategory() {
        Product prod1 = new Product("myProd1", 1000, 10.0);
        Product prod2 = new Product("myProd2", 1000, 10.0);
        Category category = new Category("testCat");
        prod1.addCategory(category);
        prod2.addCategory(category);
        em.persist(prod1);
        em.persist(prod2);
        em.persist(category);

        sut.removeProductFromCategory(category, prod1);

        Product result = em.find(Product.class, prod1.getId());
        assertFalse(result.getCategories().stream().anyMatch(c -> c.getId().equals(category.getId())));
    }

    @Test
    public void existCategoryReturnTrueIfCategoryExistAndViceVersa() {
        Category category = new Category("testCat");
        em.persist(category);

        assertTrue(sut.existCategory("testCat"));
        em.remove(category);
        assertFalse(sut.existCategory("testCat"));
    }

}
