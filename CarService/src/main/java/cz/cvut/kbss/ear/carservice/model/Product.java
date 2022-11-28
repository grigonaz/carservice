package cz.cvut.kbss.ear.carservice.model;

import cz.cvut.kbss.ear.carservice.exceptions.CategoryNotExistException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "product")
@NamedQueries({
        @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name = :name AND p.amount > 0"),
        @NamedQuery(name = "Product.findByCategory", query = "SELECT p FROM Product p WHERE (:category MEMBER OF p.categories) AND (p.amount > 0)"),
        @NamedQuery(name = "Product.sortByPriceASC", query = "SELECT p FROM Product p ORDER BY p.price ASC"),
        @NamedQuery(name = "Product.sortByPriceDESC", query = "SELECT p FROM Product p ORDER BY p.price DESC")
})
public class Product extends Abstract {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer amount;

    @Basic(optional = false)
    @Column(nullable = false)
    private Double price;

    @ManyToMany
    @OrderBy("name")
    private List<Category> categories;

    public Product(){
    }

    public Product(String name, Integer amount, Double price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public void addCategory(Category category) {
        Objects.requireNonNull(category);

        if (categories == null) {
            this.categories = new ArrayList<>();
        }

        categories.add(category);
    }

    public void removeCategory(Category category) {
        Objects.requireNonNull(category);

        if (categories == null) {
            throw new NullPointerException();
        }

        if (!categories.removeIf(c -> Objects.equals(c.getId(), category.getId()))) {
            throw new CategoryNotExistException("Attempt to remove category, which doesn't exit in list of product's categories");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}