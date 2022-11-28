package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.dto.OrderInfoDto;
import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.*;
import cz.cvut.kbss.ear.carservice.rest.util.RestUtils;
import cz.cvut.kbss.ear.carservice.security.AuthenticationToken;
import cz.cvut.kbss.ear.carservice.service.CartService;
import cz.cvut.kbss.ear.carservice.service.OrderService;
import cz.cvut.kbss.ear.carservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Void> createOrder() {
        Cart cart = userService.getCurrentUser().getCart();
        final Order order = orderService.createOrder(cart);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", order.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_EMPLOYEE')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order getOrder(@PathVariable Integer id) {
        final Order order = orderService.findById(id);
        if (order == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Order with id " + id + " is not found");
        }
        User user = userService.getCurrentUser();
        if ((user.getRole() != Role.ADMIN || user.getRole() != Role.EMPLOYEE) && !order.getClient().getId().equals(user.getId())) {
            throw new AccessDeniedException("Cannot access order of another customer");
        }
        return order;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeStatus(@PathVariable Integer id, @RequestBody OrderInfoDto status) {
        Order orderToChange = orderService.findById(id);
        if (orderToChange == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Order with id " + id + " is not found");
        }
        orderService.changeStatus(orderToChange, status.getInfo());
    }
}
