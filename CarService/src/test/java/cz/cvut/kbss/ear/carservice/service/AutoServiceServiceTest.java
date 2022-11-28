package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.model.AutoService;
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
public class AutoServiceServiceTest {

    @Autowired
    private AutoServiceService sut;

    @Test
    public void correctCreatingOfAutoService() {
        AutoService autoService = new AutoService();

        autoService.setAvailable(true);
        autoService.setName("Window Cleaning");
        autoService.setPrice(100);

        sut.createAutoService(autoService);

        assertEquals(autoService, sut.findById(autoService.getId()));
    }

    @Test
    public void findAllReturnsOnlyAvailableServices() {
        AutoService autoService = new AutoService();
        autoService.setAvailable(false);
        autoService.setName("Window Cleaning");
        autoService.setPrice(100);
        sut.createAutoService(autoService);

        List<AutoService> expected = new ArrayList<AutoService>();

        assertEquals(expected, sut.findAll());

        autoService.setAvailable(true);
        sut.update(autoService);
        expected.add(autoService);

        assertEquals(expected, sut.findAll());
    }

}
