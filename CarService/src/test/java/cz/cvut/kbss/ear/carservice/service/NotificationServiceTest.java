package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.NotificationDao;
import cz.cvut.kbss.ear.carservice.dto.RegisterDto;
import cz.cvut.kbss.ear.carservice.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class NotificationServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private NotificationService sut;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationDao notDao;

    @Test
    public void createNotificationRepairTest() {
        Repair repair = new Repair();
        repair.setRepairStatus(RepairStatus.WAITING);
        em.persist(repair);

        Notification not = sut.createNotification(repair);
        not.setId(2);
        Mockito.when(notDao.find(Mockito.anyInt())).thenReturn(not);
        assertEquals(not, sut.find(not.getId()));
    }

    @Test
    public void createNotificationOrderTest() {
        Order order = new Order();
        order.setInfo(OrderInfo.DELIVERED);
        em.persist(order);

        Notification not = sut.createNotification(order);
        not.setId(5);
        Mockito.when(notDao.find(Mockito.anyInt())).thenReturn(not);
        Notification notif = sut.find(not.getId());

        assertEquals(not, notif);
    }

    @Test
    public void notificationOrderContainsValidMessage() {
        Order order = new Order();
        order.setId(100);
        order.setInfo(OrderInfo.DELIVERED);
        em.persist(order);

        Notification not = sut.createNotification(order);
        not.setId(1);
        String messageRequires = "Your status of order with id 100 has changed to 'delivered'";
        Mockito.when(notDao.find(Mockito.anyInt())).thenReturn(not);

        assertEquals(messageRequires, sut.find(not.getId()).getMessage());
    }

    @Test
    public void notificationRepairContainsValidMessage() {
        Repair repair = new Repair();
        repair.setId(100);
        repair.setRepairStatus(RepairStatus.DONE);

        Notification not = sut.createNotification(repair);
        not.setId(1);
        Mockito.when(notDao.find(Mockito.anyInt())).thenReturn(not);
        String messageRequires = "Your status of services with id 100 has changed to 'done'";

        assertEquals(messageRequires, sut.find(not.getId()).getMessage());
    }

    @Test
    public void findAllNotificationsByUserNameTest() {
        RegisterDto userDto = new RegisterDto("testedUser", "testedUser", "testedUser", "testedUser");
        User user = userService.createNewUser(userDto);
        List<Notification> nots = createTenNotifications(user);
        createRandomNotifications();
        assertArrayEquals(nots.toArray(), sut.findAllByUserName("testedUser").toArray());
    }

    public List<Notification> createTenNotifications(User user) {
        List<Notification> nots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Repair repair = new Repair();
            repair.setRepairStatus(RepairStatus.WAITING);
            repair.setClient(user);
            repair.setCar("testCar");
            em.persist(repair);
            Notification not = sut.createNotification(repair);
            not.setId(i + 555);
            nots.add(not);
        }
        return nots;
    }

    public List<Notification> createRandomNotifications() {
        List<Notification> nots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RegisterDto userDto = new RegisterDto("testtest" + i,
                    "testtest" + i, "testtest" + i, "testtest" + i);

            User user = userService.createNewUser(userDto);

            Repair repair = new Repair();
            repair.setRepairStatus(RepairStatus.DONE);
            repair.setCar("testCar");
            repair.setClient(user);
            em.persist(repair);
            Notification not = sut.createNotification(repair);
            nots.add(not);
        }
        return nots;
    }
}
