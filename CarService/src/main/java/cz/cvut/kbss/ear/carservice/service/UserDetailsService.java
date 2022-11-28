package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.UserDao;
import cz.cvut.kbss.ear.carservice.security.UserDetails;
import cz.cvut.kbss.ear.carservice.exceptions.EntityNotFoundException;
import cz.cvut.kbss.ear.carservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new EntityNotFoundException(User.class, "username", username);
        }

        return new UserDetails(user);
    }
}
