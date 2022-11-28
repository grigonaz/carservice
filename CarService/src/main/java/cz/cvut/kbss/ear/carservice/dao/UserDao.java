package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
@Profile("!test")
public class UserDao extends BaseDao <User> {

    protected UserDao() {
        super(User.class);
    }

    public User findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
