package cz.cvut.kbss.ear.carservice.model;

import javax.persistence.*;

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = "Category.findByName", query = "SELECT c FROM Category c WHERE c.name = :name")
})
public class Category extends Abstract {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}