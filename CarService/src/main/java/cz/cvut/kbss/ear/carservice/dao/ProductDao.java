package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.Category;
import cz.cvut.kbss.ear.carservice.model.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class ProductDao extends BaseDao<Product> {

    public ProductDao() {
        super(Product.class);
    }

    @Override
    public List<Product> findAll() {
        return em.createQuery("select p from Product p where p.amount > 0", Product.class).getResultList();
    }

    public List<Product> sortByPriceASC() {
        return em.createNamedQuery("Product.sortByPriceASC", Product.class).getResultList();
    }

    public List<Product> sortByPriceDESC() {
        return em.createNamedQuery("Product.sortByPriceDESC", Product.class).getResultList();
    }

    public List<Product> findByCategory(Category category) {
        try {
            return em.createNamedQuery("Product.findByCategory", Product.class).setParameter("category", category)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

    public Product findByName(String name) {
        try {
            return em.createNamedQuery("Product.findByName", Product.class).setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
