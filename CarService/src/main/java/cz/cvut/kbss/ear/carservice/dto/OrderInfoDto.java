package cz.cvut.kbss.ear.carservice.dto;

import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.OrderInfo;
import cz.cvut.kbss.ear.carservice.model.Role;

public class OrderInfoDto {

    String orderInfoStr;
    OrderInfo info;

    public OrderInfo getInfo() {
        return info;
    }

    public void setInfo(String roleStr) {
        switch (roleStr) {
            case "ORDERED":
                this.info = OrderInfo.ORDERED;
                break;
            case "DELIVERED":
                this.info = OrderInfo.DELIVERED;
                break;
            case "ONTHEWAY":
                this.info = OrderInfo.ONTHEWAY;
                break;
            default:
                throw new NotFoundException("Status with name " + orderInfoStr + " doesn't exist");
        }
    }
}
