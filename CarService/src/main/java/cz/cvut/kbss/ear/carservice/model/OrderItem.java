package cz.cvut.kbss.ear.carservice.model;

import javax.persistence.*;

@Entity
public class OrderItem extends Item {

    @Enumerated(EnumType.STRING)
    private OrderItemInfo info;

    public OrderItem() {
    }

    public OrderItem(CartItem item) {
        setAmount(item.getAmount());
        setProduct(item.getProduct());
        this.info = OrderItemInfo.ORDERED;
    }

    public OrderItemInfo getInfo() {
        return info;
    }

    public void setInfo(OrderItemInfo info) {
        this.info = info;
    }
}

