package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.CarServiceApp;
import cz.cvut.kbss.ear.carservice.environment.Generator;
import cz.cvut.kbss.ear.carservice.model.AutoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = CarServiceApp.class)
public class AutoServiceDaoTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private AutoServiceDao sut;

    @Test
    public void findAllAutoServicesReturnListOfAllAutoservices() {
        List<AutoService> autoServices = new ArrayList<>();
        List<AutoService> autoServicesReturned = new ArrayList<>();

        int count = 0;

        for (int i = 0; i < 10; i++) {
            autoServices.add(generateAutoservice());
            count++;
        }

        autoServicesReturned = sut.findAll();

        assertEquals(count, autoServicesReturned.size());
    }

    private AutoService generateAutoservice() {
        AutoService autoService = new AutoService("Test" + Generator.randomInt(), 100.0);
        autoService.setAvailable(true);
        em.persist(autoService);
        return autoService;
    }
}
