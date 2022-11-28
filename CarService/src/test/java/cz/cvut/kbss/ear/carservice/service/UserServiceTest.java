package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dto.RegisterDto;
import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.exceptions.PermissionException;
import cz.cvut.kbss.ear.carservice.model.Role;
import cz.cvut.kbss.ear.carservice.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService sut;

    @Test
    public void createNewUserThrowsInvalidDataExceptionWhenUsernameAlreadyExist() {
        User user = sut.createNewUser(new RegisterDto("FirstName", "LastName", "usernozar", "user123"));
        RegisterDto user2 = new RegisterDto("FirstName2", "LastName2", "usernozar", "ggggfsf");

        assertThrows(InvalidDataException.class, () -> sut.createNewUser(user2));
    }

    @Test
    public void createNewUserAllowedOnlyLettersNumbersAndUnderscore6_30Symbols() {
        User user = sut.createNewUser(new RegisterDto("FirstName2", "LastName2", "userus", "ggggfsf"));

        User found = em.find(User.class, user.getId());

        assertEquals(user, found);

        RegisterDto testUser1 = new RegisterDto("FirstName", "LastName", "safa+sgfa", "user123");
        assertThrows(InvalidDataException.class, () -> sut.createNewUser(testUser1));

        RegisterDto testUser2 = new RegisterDto("FirstName2", "LastName2", "user", "ggggfsf");
        assertThrows(InvalidDataException.class, () -> sut.createNewUser(testUser2));

        RegisterDto testUser3 = new RegisterDto("FirstName2", "LastName2", "useruseruseruseruseruseruseruseruseruseruseruseruseruseruser", "ggggfsf");
        assertThrows(InvalidDataException.class, () -> sut.createNewUser(testUser3));
    }

    @Test
    public void existCategoryReturnTrueIfCategoryExistAndViceVersa() {
        User user = new User("FirstName2", "LastName2", "testUser", "ggggfsf");
        em.persist(user);

        assertTrue(sut.existUser("testUser"));
        em.remove(user);
        assertFalse(sut.existUser("testUser"));
    }

    @Test
    public void changeUserRoleThrowsPermissionExceptionIfChangerIsNotAdmin() {
        User admin = new User("admin", "admin", "administrator", "admin");
        User user = new User("first", "first", "firstfirst", "user");
        admin.setRole(Role.EMPLOYEE);
        em.persist(admin);
        em.persist(user);
        assertThrows(PermissionException.class, () -> sut.changeUserRole(admin, user, Role.EMPLOYEE));
        admin.setRole(Role.CLIENT);
        em.persist(admin);
        assertThrows(PermissionException.class, () -> sut.changeUserRole(admin, user, Role.EMPLOYEE));
    }

    @Test
    public void changUserRoleChangesRole() {
        User admin = new User("admin", "admin", "administrator", "admin");
        User user = new User("first", "first", "firstfirst", "user");
        admin.setRole(Role.ADMIN);
        user.setRole(Role.CLIENT);
        em.persist(admin);
        em.persist(user);
        sut.changeUserRole(admin, user, Role.EMPLOYEE);

        User result = em.find(User.class, user.getId());
        assertEquals(Role.EMPLOYEE, result.getRole());
    }

}


