package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.Product;
import cz.cvut.kbss.ear.carservice.rest.util.RestUtils;
import cz.cvut.kbss.ear.carservice.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProducts() {
        return productService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        productService.createProduct(product);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", product.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable Integer id) {
        final Product product = productService.findById(id);
        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product with id " + id + " is not found" );
        }
        return product;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        final Product got = getProduct(id);
        if (!got.getId().equals(product.getId())) {
            throw new InvalidDataException("Product identifier in the data does not match the one in the request URL.");
        }
        productService.update(product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @DeleteMapping(value = "/{id}")
    public void removeProduct(@PathVariable Integer id) {
        final Product product = productService.findById(id);
        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product with id " + id + " is not found" );
        }
        productService.delete(product);
    }
}
