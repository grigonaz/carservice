package cz.cvut.kbss.ear.carservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.kbss.ear.carservice.exceptions.EmptyCartException;
import cz.cvut.kbss.ear.carservice.exceptions.ItemNotExistException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "repaircart")
public class RepairCart extends Abstract {

    @JsonIgnore
    @OneToOne(mappedBy = "repairCart", optional = false)
    private User owner;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "repaiservice_id")
    private List<ServiceItem> serviceItems;

    public RepairCart() {
    }

    public void addItem(ServiceItem item){
        if (serviceItems == null) {
            serviceItems = new ArrayList<>();
        }

        Optional<ServiceItem> op = serviceItems.stream().filter(exIt -> exIt == item).findFirst();

        if (op.isPresent()) {
            ServiceItem exIt = op.get();
            exIt.setAmount(exIt.getAmount() + item.getAmount());
        } else {
            serviceItems.add(item);
        }
    }

    public void removeItem(ServiceItem item){
        if (serviceItems == null) {
            throw new EmptyCartException("Attempt to remove item without items in it");
        }

        ServiceItem op = serviceItems.stream().filter(exIt -> exIt.getId().equals(item.getId())).findFirst().orElse(null);

        if (op != null) {
            op.setAmount(op.getAmount() - item.getAmount());

            if (op.getAmount() <= 0) {
                serviceItems.remove(op);
            }
        } else {
            throw new ItemNotExistException("Attempt to remove item, which is not in the cart");
        }
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<ServiceItem> getItems() {
        return serviceItems;
    }

    public void setItems(List<ServiceItem> items) {
        this.serviceItems = items;
    }
}
