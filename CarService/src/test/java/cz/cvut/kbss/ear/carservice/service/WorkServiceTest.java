package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.UserDao;
import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class WorkServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private WorkService sut;

    private User user;
    private User employee1;
    private User employee2;
    private Repair repair;

    @Before
    public void setUp() {
        employee1 = new User();
        employee1.setUsername("TestEmployee");
        employee1.setFirstName("TestName");
        employee1.setLastName("TestLastName");
        employee1.setRole(Role.EMPLOYEE);
        employee1.setPassword("testPassword");
        employee1.setMoney(100d);
        em.persist(employee1);

        employee2 = new User();
        employee2.setUsername("TestEmployee2");
        employee2.setFirstName("TestName2");
        employee2.setLastName("TestLastName2");
        employee2.setPassword("testPassword");
        employee2.setRole(Role.EMPLOYEE);
        employee2.setMoney(100d);
        em.persist(employee2);

        user = new User();
        user.setUsername("TestUser");
        user.setFirstName("TestName");
        user.setLastName("TestLastName");
        user.setPassword("testPassword");
        user.setRole(Role.CLIENT);
        user.setMoney(100d);
        em.persist(user);

        repair = new Repair();
        repair.setCar("testcar");
        repair.setRepairStatus(RepairStatus.DONE);
        repair.setClient(user);
        em.persist(repair);
    }

    @Test
    public void createCorrectWorkChangesRepairEmployeesAndWorks() {
        Mockito.when(userService.getCurrentUser()).thenReturn(employee1);
        Work work = new Work();
        work.setRepair(repair);
        work.setEmployee(employee1);
        work.setBeginWork(LocalDateTime.of(2021, 1, 10, 14, 30));
        work.setFinishWork(LocalDateTime.of(2021, 1, 10, 16, 30));

        sut.create(work);

        assertEquals(work, sut.findById(work.getId()));
        assertEquals(employee1.getWorks().get(0), sut.findById(work.getId()));
        assertEquals(repair.getWorks().get(0), sut.findById(work.getId()));
    }

    @Test
    public void findAllByEmployeeIdReturnsCorrectWorks() {
        Mockito.when(userService.getCurrentUser()).thenReturn(employee1);
        Work work1 = new Work();
        work1.setRepair(repair);
        work1.setEmployee(employee1);
        work1.setBeginWork(LocalDateTime.of(2021, 1, 10, 14, 30));
        work1.setFinishWork(LocalDateTime.of(2021, 1, 10, 16, 30));
        sut.create(work1);

        Mockito.when(userService.getCurrentUser()).thenReturn(employee2);
        Work work2 = new Work();
        work2.setRepair(repair);
        work2.setEmployee(employee2);
        work2.setBeginWork(LocalDateTime.of(2021, 1, 10, 14, 30));
        work2.setFinishWork(LocalDateTime.of(2021, 1, 10, 15, 30));
        sut.create(work2);

        Mockito.when(userService.findUser(employee1.getId())).thenReturn(employee1);
        Mockito.when(userService.findUser(employee2.getId())).thenReturn(employee2);
        assertEquals(work1, sut.findAllByEmployeeId(employee1.getId()).get(0));
        assertEquals(work2, sut.findAllByEmployeeId(employee2.getId()).get(0));
    }

    @Test
    public void createWorksWithIntersectionsOrInvalidDataThrowsInvalidDataException() {
        Mockito.when(userService.getCurrentUser()).thenReturn(employee1);
        Work work1 = new Work();
        work1.setRepair(repair);
        work1.setEmployee(employee1);
        //END OF WORK IS BEFORE START
        work1.setBeginWork(LocalDateTime.of(2021, 1, 10, 14, 30));
        work1.setFinishWork(LocalDateTime.of(2021, 1, 10, 13, 30));


        assertThrows(InvalidDataException.class, () -> sut.create(work1));

        work1.setBeginWork(LocalDateTime.of(2021, 1, 10, 14, 30));
        work1.setFinishWork(LocalDateTime.of(2021, 1, 10, 16, 30));
        sut.create(work1);

        Work work2 = new Work();
        work2.setRepair(repair);
        work2.setEmployee(employee1);
        //INTERSECTION WITH WORK1
        work2.setBeginWork(LocalDateTime.of(2021, 1, 10, 14, 30));
        work2.setFinishWork(LocalDateTime.of(2021, 1, 10, 15, 30));
        assertThrows(InvalidDataException.class, () -> sut.create(work2));
    }

}
