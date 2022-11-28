package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.BannedJWTDao;
import cz.cvut.kbss.ear.carservice.model.BannedJWT;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BannedJWTService {

    private final BannedJWTDao dao;

    public BannedJWTService(BannedJWTDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void persist(BannedJWT token) {
        dao.persist(token);
    }

    @Transactional(readOnly = true)
    public boolean exist(String token){
        return dao.exist(token) != null;
    }
}
