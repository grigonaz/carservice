package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.BannedJWT;
import org.springframework.stereotype.Repository;
import javax.persistence.NoResultException;

@Repository
public class BannedJWTDao extends BaseDao<BannedJWT> {

    protected BannedJWTDao() {
        super(BannedJWT.class);
    }

    public BannedJWT exist(String token) {
        try {
            return em.createQuery("select b from BannedJWT b where b.token = :token", BannedJWT.class).setParameter("token", token)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
