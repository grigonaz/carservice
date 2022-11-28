package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.Category;
import cz.cvut.kbss.ear.carservice.model.Product;
import cz.cvut.kbss.ear.carservice.rest.util.RestUtils;
import cz.cvut.kbss.ear.carservice.service.CategoryService;
import cz.cvut.kbss.ear.carservice.service.ProductService;
import cz.cvut.kbss.ear.carservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(CategoryService categoryService, UserService userService, ProductService productService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        LOG.debug("Created category {}.", category);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", category.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Category getById(@PathVariable Integer id) {
        final Category category = categoryService.findById(id);
        if (category == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id " + id + " is not found");
        }
        return category;
    }

    @GetMapping(value = "/{id}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProductsByCategory(@PathVariable Integer id) {
        List<Product> products = productService.findByCategory(id);
        if (products == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id " + id + " is not found");
        }
        return products;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    @PostMapping(value = "/{id_cat}/products/{id_prod}")
    public void addProductToCategory(@PathVariable Integer id_cat, @PathVariable Integer id_prod) {
        final Category category = getById(id_cat);
        if (category == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id " + id_cat + " is not found");
        }
        Product product = productService.findById(id_prod);
        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product with id " + id_prod + " is not found");
        }
        categoryService.addProductToCategory(category, product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping(value = "/{categoryId}/products/{productId}")
    public void removeProductFromCategory(@PathVariable Integer categoryId, @PathVariable Integer productId) {
        final Category category = getById(categoryId);
        if (category == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id " + categoryId + " is not found");
        }
        final Product toRemove = productService.findById(productId);
        if (toRemove == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product with id " + productId + " is not found");
        }

        categoryService.removeProductFromCategory(category, toRemove);
    }
}
