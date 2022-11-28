package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class CategoryDao extends BaseDao <Category> {

    protected CategoryDao() {
        super(Category.class);
    }

    public Category findByName(String name) {
        try {
            return em.createNamedQuery("Category.findByName", Category.class).setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

