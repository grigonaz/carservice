package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.*;
import cz.cvut.kbss.ear.carservice.dao.UserDao;
import cz.cvut.kbss.ear.carservice.dto.RegisterDto;
import cz.cvut.kbss.ear.carservice.dto.RoleDto;
import cz.cvut.kbss.ear.carservice.exceptions.EntityNotFoundException;
import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.exceptions.PermissionException;
import cz.cvut.kbss.ear.carservice.model.*;
import cz.cvut.kbss.ear.carservice.rest.util.Constants;
import cz.cvut.kbss.ear.carservice.rest.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;


@Service
@Profile("!test")
public class UserService {

    private final UserDao userDao;
    private final CartDao cartDao;
    private final RepairCartDao repairCartDao;

    @Autowired
    public UserService(UserDao userDao, CartDao cartDao, RepairCartDao repairCartDao) {
        this.userDao = userDao;
        this.cartDao = cartDao;
        this.repairCartDao = repairCartDao;
    }

    @Transactional
    public void changeUserRole(User changer, User user, Role role) {
        Objects.requireNonNull(changer);
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);

        if (changer.getRole() != Role.ADMIN) {
            throw new PermissionException("Attempt to change the user role with the role " + user.getRole());
        }

        user.setRole(role);
        userDao.update(user);

    }

    @Transactional
    public User createNewUser(RegisterDto registerDto) {
        User user = new User();

        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(registerDto.getPassword()));
        user.setRole(Role.CLIENT);
        user.setMoney(0d);

        String firstName = registerDto.getFirstName();
        String lastName = registerDto.getLastName();
        String username = registerDto.getUsername();
        String password = registerDto.getPassword();

        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        if (existUser(username)) {
            throw new InvalidDataException("User with username" + username + "already exist");
        }

        if (!Validator.isUsernameValid(username)) {
            throw new InvalidDataException("Username can contains numbers, letters and symbol <_>. Length must be 6-30 symbols" +
                    " You have entered username <" + username + "> ");
        }

        userDao.persist(user);

        if (user.getCart() == null) {
            Cart cart = new Cart();
            cart.setOwner(user);
            user.setCart(cart);
            cartDao.persist(cart);
            userDao.update(user);
        }

        if (user.getRepairCart() == null) {
            RepairCart rCart = new RepairCart();
            rCart.setOwner(user);
            user.setRepairCart(rCart);
            repairCartDao.persist(rCart);
            userDao.update(user);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public boolean existUser(String username) {
        return userDao.findByUsername(username) != null;
    }

    @Transactional(readOnly = true)
    public User findUser(Integer id) {
        return userDao.find(id);
    }

    @Transactional
    public void persist(User user) {
        Objects.requireNonNull(user);

        if (user.getRole() == null) {
            user.setRole(Constants.DEFAULT_ROLE);
        }

        userDao.persist(user);
    }

    @Transactional
    public void update(User employee) {
        userDao.update(employee);
    }

    @Transactional
    public void setMoney(Double money, String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw NotFoundException.create("User", username);
        }
        user.setMoney(money);
        update(user);
    }

    @Transactional
    public void setRole(RoleDto roleDto, String username) {
        User user = userDao.findByUsername(username);
        Role role = roleDto.getRole();
        if (role == null) {
            throw new InvalidDataException("Roles are ROLE_ADMIN, ROLE_CLIENT AND ROLE_EMPLOYEE");
        }
        if (user == null) {
            throw NotFoundException.create("User", username);
        }
        user.setRole(role);
        update(user);
    }

    public User getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization").split(" ")[1];

        return findOrThrowByUsername(new JWTUtil().extractUsername(token));
    }


    @Transactional
    public User findOrThrowByUsername(String username) {
        return Optional.ofNullable(userDao.findByUsername(username)).orElseThrow(
                () -> new EntityNotFoundException(User.class, "username", username)
        );
    }
}
