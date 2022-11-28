package cz.cvut.kbss.ear.carservice.service.testconfig;

import cz.cvut.kbss.ear.carservice.dao.NotificationDao;
import cz.cvut.kbss.ear.carservice.dao.UserDao;
import cz.cvut.kbss.ear.carservice.model.Product;
import cz.cvut.kbss.ear.carservice.model.User;
import cz.cvut.kbss.ear.carservice.service.ProductService;
import cz.cvut.kbss.ear.carservice.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.persistence.EntityManager;

@Profile("test")
@Configuration
public class TestConfig {

    @Bean
    @Primary
    public UserDao userDao() {
        return Mockito.mock(UserDao.class);
    }

    @Bean
    @Primary
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    @Primary
    public ProductService productService() {
        return Mockito.mock(ProductService.class);
    }

    @Bean
    @Primary
    public NotificationDao notificationDao() {
        return Mockito.mock(NotificationDao.class);
    }

}
