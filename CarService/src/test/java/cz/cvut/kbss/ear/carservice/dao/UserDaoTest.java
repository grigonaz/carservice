package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.CarServiceApp;
import cz.cvut.kbss.ear.carservice.environment.Generator;
import cz.cvut.kbss.ear.carservice.model.User;
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
public class UserDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao sut;

    @Test
    public void findByUsernameReturnsPersonWithThisUsername() {
        final User user = generateUser();
        em.persist(user);

        final User result = sut.findByUsername(user.getUsername());
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    private static User generateUser() {
        return new User("Test", "Test", "test" + Generator.randomInt(), "test123");
    }

}
