package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.CarServiceApp;
import cz.cvut.kbss.ear.carservice.environment.Generator;
import cz.cvut.kbss.ear.carservice.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = CarServiceApp.class)
public class CategoryDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CategoryDao sut;

    @Test
    public void findByNameReturnsCategoryWithThisName() {
        final Category c = generateCategory();
        em.persist(c);

        final Category result = sut.findByName(c.getName());
        assertNotNull(result);
        assertEquals(c.getName(), result.getName());
    }

    private static Category generateCategory() {
        final Category c = new Category();
        c.setName("Test category " + Generator.randomInt());
        return c;
    }
}
