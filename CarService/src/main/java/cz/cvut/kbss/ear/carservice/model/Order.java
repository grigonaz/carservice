package cz.cvut.kbss.ear.carservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.kbss.ear.carservice.exceptions.EmptyCartException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orderorder")
public class Order extends Abstract {

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private OrderInfo info;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private User client;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    public Order() {
    }

    public Order(Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("Attempt to create the Order with empty cart");
        }

        this.client = cart.getOwner();
        this.created = LocalDateTime.now();
        this.info = OrderInfo.ORDERED;
        this.items = cart.getItems().stream().map(OrderItem::new).collect(Collectors.toList());

        cart.getItems().clear();
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public OrderInfo getInfo() {
        return info;
    }

    public void setInfo(OrderInfo info) {
        this.info = info;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
