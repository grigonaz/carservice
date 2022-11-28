package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.dto.OrderItemInfoDto;
import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.*;
import cz.cvut.kbss.ear.carservice.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/orders/items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeStatus(@PathVariable Integer id, @RequestBody OrderItemInfoDto status) {
        OrderItem orderItemToChange = (OrderItem) orderItemService.find(id);
        if (orderItemToChange == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "orderItem with id " + id + " is not found");
        }
        orderItemService.changeStatus(orderItemToChange, status.getInfo());
    }
}
