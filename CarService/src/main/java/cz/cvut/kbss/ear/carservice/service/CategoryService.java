package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.CategoryDao;
import cz.cvut.kbss.ear.carservice.dao.ProductDao;
import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.model.Category;
import cz.cvut.kbss.ear.carservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @Transactional
    public void addProductToCategory(Category category, Product product) {
        Objects.requireNonNull(category);
        Objects.requireNonNull(product);
        product.addCategory(category);
        productDao.update(product);
    }

    @Transactional
    public void removeProductFromCategory(Category category, Product product) {
        Objects.requireNonNull(category);
        Objects.requireNonNull(product);
        product.removeCategory(category);
        productDao.update(product);
    }

    @Transactional
    public Category createCategory(Category category){
        Objects.requireNonNull(category.getName());

        if (existCategory(category.getName())) {
            throw new InvalidDataException("Category with name <" + category.getName() + "> already exist");
        }

        if (category.getName().equals("")) {
            throw new InvalidDataException("Attempt to create the category with this data: name: " + category.getName());
        }

        categoryDao.persist(category);

        return category;
    }

    @Transactional(readOnly = true)
    public boolean existCategory(String username) {
        return categoryDao.findByName(username) != null;
    }

    @Transactional(readOnly = true)
    public Category findById(Integer id) {
        return categoryDao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public void persist(Category category) {
        categoryDao.persist(category);
    }
}
