package cz.cvut.kbss.ear.carservice.model;

import javax.persistence.*;

@Entity
@Table(name = "item")
public abstract class Item extends Abstract {

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Product product;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}