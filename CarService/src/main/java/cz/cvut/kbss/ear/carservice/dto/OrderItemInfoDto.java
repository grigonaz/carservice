package cz.cvut.kbss.ear.carservice.dto;

import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.OrderInfo;
import cz.cvut.kbss.ear.carservice.model.OrderItemInfo;

public class OrderItemInfoDto {

    String orderItemInfoStr;
    OrderItemInfo info;

    public OrderItemInfo getInfo() {
        return info;
    }

    public void setInfo(String orderItemInfoStr) {
        switch (orderItemInfoStr) {
            case "ORDERED":
                this.info = OrderItemInfo.ORDERED;
                break;
            case "DELIVERED":
                this.info = OrderItemInfo.DELIVERED;
                break;
            case "ONTHEWAY":
                this.info = OrderItemInfo.ONTHEWAY;
                break;
            default:
                throw new NotFoundException("Status with name " + orderItemInfoStr + " doesn't exist");
        }
    }
}
