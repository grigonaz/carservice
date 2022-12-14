package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.environment.Generator;
import cz.cvut.kbss.ear.carservice.CarServiceApp;
import cz.cvut.kbss.ear.carservice.exceptions.PersistenceException;
import cz.cvut.kbss.ear.carservice.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = CarServiceApp.class)
public class BaseDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CategoryDao sut;

    @Test
    public void persistSavesSpecifiedInstance() {
        final Category cat = generateCategory();
        sut.persist(cat);
        assertNotNull(cat.getId());

        final Category result = em.find(Category.class, cat.getId());
        assertNotNull(result);
        assertEquals(cat.getId(), result.getId());
        assertEquals(cat.getName(), result.getName());
    }

    private static Category generateCategory() {
        final Category cat = new Category();
        cat.setName("Test category " + Generator.randomInt());
        return cat;
    }

    @Test
    public void findRetrievesInstanceByIdentifier() {
        final Category cat = generateCategory();
        em.persistAndFlush(cat);
        assertNotNull(cat.getId());

        final Category result = sut.find(cat.getId());
        assertNotNull(result);
        assertEquals(cat.getId(), result.getId());
        assertEquals(cat.getName(), result.getName());
    }

    @Test
    public void findAllRetrievesAllInstancesOfType() {
        final Category catOne = generateCategory();
        em.persistAndFlush(catOne);
        final Category catTwo = generateCategory();
        em.persistAndFlush(catTwo);

        final List<Category> result = sut.findAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(catOne.getId())));
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(catTwo.getId())));
    }

    @Test
    public void updateUpdatesExistingInstance() {
        final Category cat = generateCategory();
        em.persistAndFlush(cat);

        final Category update = new Category();
        update.setId(cat.getId());
        final String newName = "New category name";
        update.setName(newName);
        sut.update(update);

        final Category result = sut.find(cat.getId());
        assertNotNull(result);
        assertEquals(cat.getName(), result.getName());
    }

    @Test
    public void removeRemovesSpecifiedInstance() {
        final Category cat = generateCategory();
        em.persistAndFlush(cat);
        assertNotNull(em.find(Category.class, cat.getId()));
        em.detach(cat);

        sut.remove(cat);
        assertNull(em.find(Category.class, cat.getId()));
    }

    @Test
    public void removeDoesNothingWhenInstanceDoesNotExist() {
        final Category cat = generateCategory();
        cat.setId(123);
        assertNull(em.find(Category.class, cat.getId()));

        sut.remove(cat);
        assertNull(em.find(Category.class, cat.getId()));
    }

    @Test(expected = PersistenceException.class)
    public void exceptionOnPersistInWrappedInPersistenceException() {
        final Category cat = generateCategory();
        em.persistAndFlush(cat);
        em.remove(cat);
        sut.update(cat);
    }

    @Test
    public void existsReturnsTrueForExistingIdentifier() {
        final Category cat = generateCategory();
        em.persistAndFlush(cat);
        assertTrue(sut.exists(cat.getId()));
        assertFalse(sut.exists(-1));
    }
}
