package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.CarServiceApp;
import cz.cvut.kbss.ear.carservice.environment.Generator;
import cz.cvut.kbss.ear.carservice.model.Category;
import cz.cvut.kbss.ear.carservice.model.Order;
import cz.cvut.kbss.ear.carservice.model.Product;
import cz.cvut.kbss.ear.carservice.service.SystemInitializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
// DataJpaTest does not load all the application beans, it starts only persistence-related stuff
@DataJpaTest
// Exclude SystemInitializer from the startup, we don't want the admin account here
@ComponentScan(basePackageClasses = CarServiceApp.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SystemInitializer.class)})
public class ProductDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProductDao sut;

    @Test
    public void findAllProductReturnListOfAllProductsWithAmountBigger0() {
        List<Product> prods = new ArrayList<>();
        List<Product> prodsRet = new ArrayList<>();
        int countPositive = 0;
        for (int i = 0; i < 10; i++) {
            prods.add(generateProduct());
            if (prods.get(i).getAmount() > 0) {
                countPositive++;
            }
        }
        prodsRet = sut.findAll();
        assertEquals(countPositive, prodsRet.size());
    }

    @Test
    public void findByNameTestReturnsProductWithAmountMoreThen0() {
        for (int i = 0; i < 10; i++) {
            generateProduct();
        }
        Product product = new Product("myProd", 1, 10.0);
        sut.persist(product);
        Product found = sut.findByName("myProd");
        assertEquals(product, found);
    }

    @Test
    public void findByNameTestReturnsNullWithAmountLessThen0() {
        for (int i = 0; i < 10; i++) {
            generateProduct();
        }
        Product product = new Product("myProd", -1000, 10.0);
        sut.persist(product);
        Product found = sut.findByName("myProd");
        assertNull(found);
    }

    @Test
    public void findByCategoryNameReturnsOnlyProdsWithPositiveAmountAndCorrectProducts() {
        Product prod1 = new Product("myProd1", 1000, 10.0);
        Product prod2 = new Product("myProd2", -1000, 10.0);
        Product prod3 = new Product("myProd3", 0, 10.0);
        Product prod4 = new Product("myProd1", 3, 10.0);
        sut.persist(prod1);
        sut.persist(prod2);
        sut.persist(prod3);
        sut.persist(prod4);

        Category category1 = new Category("cat1");
        Category category2 = new Category("cat2");
        Category category3 = new Category("cat3");
        em.persist(category1);
        em.persist(category2);
        em.persist(category3);

        prod1.addCategory(category2);
        prod1.addCategory(category3);

        prod2.addCategory(category2);

        prod3.addCategory(category1);

        prod4.addCategory(category2);

        sut.update(prod1);
        sut.update(prod2);
        sut.update(prod3);

        List<Product> correctProd1 = new ArrayList<>();
        List<Product> correctProd2 = new ArrayList<>();
        correctProd2.add(prod4);
        correctProd2.add(prod1);
        List<Product> correctProd3 = new ArrayList<>();
        correctProd3.add(prod1);

        List<Product> products1cat = sut.findByCategory(category1);
        List<Product> products2cat = sut.findByCategory(category2);
        List<Product> products3cat = sut.findByCategory(category3);

        assertEquals(correctProd1.toArray().length, products1cat.toArray().length);
        assertEquals(correctProd2.toArray().length, products2cat.toArray().length);
        assertEquals(correctProd3.toArray().length, products3cat.toArray().length);
    }

    private Product generateProduct() {
        Product product = new Product("testProd" + Generator.randomInt(), Generator.randomInt(), 10.0);
        sut.persist(product);
        return product;
    }
}
