package cz.cvut.kbss.ear.carservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.kbss.ear.carservice.exceptions.EmptyCartException;
import cz.cvut.kbss.ear.carservice.exceptions.ItemNotExistException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "cart")
public class Cart extends Abstract {

    @JsonIgnore
    @OneToOne(mappedBy = "cart", optional = false)
    private User owner;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> cartItems;

    public Cart() {
    }

    public void addItem(CartItem item){
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        CartItem op = cartItems.stream()
                .filter(exIt -> exIt.getId().equals(item.getId()))
                .findFirst()
                .orElse(null);

        if (op != null) {
            op.setAmount(op.getAmount() + item.getAmount());
        } else {
            cartItems.add(item);
        }
    }



    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<CartItem> getItems() {
        return cartItems;
    }

    public void setItems(List<CartItem> items) {
        this.cartItems = items;
    }
}