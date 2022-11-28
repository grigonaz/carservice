package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.CategoryDao;
import cz.cvut.kbss.ear.carservice.dao.ProductDao;
import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Profile("!test")
public class ProductService {

    private final ProductDao productDao;
    private final CategoryDao categoryDao;

    @Autowired
    public ProductService(ProductDao productDao, CategoryDao categoryDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
    }

    @Transactional
    public Product createProduct(Product product) {
        String name = product.getName();
        Integer amount = product.getAmount() ;
        Double price = product.getPrice();

        Objects.requireNonNull(name);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(price);

        if (existProduct(name)) {
            throw new InvalidDataException("Product with name" + name + "already exist");
        }

        if (name.equals("") || amount < 0 || price < 0) {
            throw new InvalidDataException("Attempt to create the product with this data: name: " +
                    name + " amount: " + amount + " price: " + price);
        }

        productDao.persist(product);

        return product;
    }

    @Transactional(readOnly = true)
    public boolean existProduct(String username) {
        return productDao.findByName(username) != null;
    }

    @Transactional(readOnly = true)
    public Product findByName(String name) {
        return productDao.findByName(name);
    }

    @Transactional(readOnly = true)
    public Product findById(Integer id) {
        return productDao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Transactional
    public void update(Product product) {
        productDao.update(product);
    }

    @Transactional
    public void delete(Product product){
        product.setAmount(0);
        update(product);
    }

    @Transactional
    public List<Product> findByCategory(Integer id) {
        return productDao.findByCategory(categoryDao.find(id));
    }
}
