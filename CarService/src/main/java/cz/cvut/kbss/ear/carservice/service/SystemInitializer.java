package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dto.LoginDto;
import cz.cvut.kbss.ear.carservice.dto.RegisterDto;
import cz.cvut.kbss.ear.carservice.model.Cart;
import cz.cvut.kbss.ear.carservice.model.Role;
import cz.cvut.kbss.ear.carservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("!test")
public class SystemInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(SystemInitializer.class);

    /**
     * Default admin username
     */
    private static final String ADMIN_USERNAME = "adminUser";

    private final UserService userService;

    @Autowired
    public SystemInitializer(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    private void initSystem() {
        this.generateAdmin();
    }

    /**
     * Generates an admin account if it does not already exist.
     */
    private void generateAdmin() {
        if (!userService.existUser(ADMIN_USERNAME)) {
            RegisterDto adminDto = new RegisterDto("Administrator","System",ADMIN_USERNAME,"adm1n");
            User admin = userService.createNewUser(adminDto);
            admin.setRole(Role.ADMIN);
            admin.setMoney(0.000001);
            LOG.info("Generated admin user with credentials " + admin.getUsername() + "/" + admin.getPassword());
            userService.update(admin);
        }
    }

}
