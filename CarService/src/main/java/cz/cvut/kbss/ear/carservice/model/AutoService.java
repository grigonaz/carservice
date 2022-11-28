package cz.cvut.kbss.ear.carservice.model;

import javax.persistence.*;

@Entity
@Table(name = "autoservice")
@NamedQueries({
        @NamedQuery(name = "AutoService.findByName", query = "SELECT s FROM AutoService s WHERE s.name = :name"),
        @NamedQuery(name = "AutoService.findAllAvailable", query = "SELECT s FROM AutoService s WHERE s.available = true")
})
public class AutoService extends Abstract {

    public AutoService() {
    }

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private double price;

    @Basic(optional = false)
    @Column(nullable = false)
    private boolean available;

    public AutoService(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean availability) {
        this.available = availability;
    }
}
