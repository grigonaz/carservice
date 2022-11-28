package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.CarServiceApp;
import cz.cvut.kbss.ear.carservice.model.ServiceItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = CarServiceApp.class)
public class ServiceItemDaoTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ServiceItemDao sut;

    @Test
    public void findServiceItemById() {
        final ServiceItem serviceItem = generateServiceItem();
        em.persist(serviceItem);
        assertNotNull(serviceItem.getId());

        final ServiceItem result = sut.find(serviceItem.getId());
        assertNotNull(result);
        assertEquals(serviceItem.getId(), result.getId());
    }

    private ServiceItem generateServiceItem() {
        ServiceItem serviceItem = new ServiceItem();
        sut.persist(serviceItem);
        return serviceItem;
    }
}
