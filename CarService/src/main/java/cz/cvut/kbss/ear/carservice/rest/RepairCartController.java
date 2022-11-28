package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.*;
import cz.cvut.kbss.ear.carservice.service.AutoServiceService;
import cz.cvut.kbss.ear.carservice.service.RepairCartService;
import cz.cvut.kbss.ear.carservice.service.ServiceItemService;
import cz.cvut.kbss.ear.carservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/repaircarts")
public class RepairCartController {

    private final RepairCartService repairCartService;
    private final UserService userService;
    private final AutoServiceService autoSService;
    private final ServiceItemService serviceItemService;

    @Autowired
    public RepairCartController(RepairCartService repairCartService, UserService userService, AutoServiceService autoSService, ServiceItemService serviceItemService) {
        this.repairCartService = repairCartService;
        this.userService = userService;
        this.autoSService = autoSService;
        this.serviceItemService = serviceItemService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RepairCart getRepairCart() {
        User user = userService.getCurrentUser();
        return user.getRepairCart();
    }

    @PutMapping(value = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addRepairItem(@RequestBody ServiceItem item) {
        AutoService autoService = autoSService.findById(item.getAutoService().getId());
        if (autoService == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "AutoService with id " + item.getAutoService().getId() + " is not found" );
        }
        User user = userService.getCurrentUser();
        RepairCart repairCart = user.getRepairCart();
        repairCartService.addItem(repairCart, item);
    }

    @DeleteMapping(value = "/items/{id}")
    public void removeRepairItem(@PathVariable Integer id) {
        ServiceItem itemToRemove = serviceItemService.findById(id);
        if (itemToRemove == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ServiceItem with id " + id + " is not found" );
        }
        User user = userService.getCurrentUser();
        RepairCart repairCart = user.getRepairCart();
        repairCartService.removeItem(repairCart, itemToRemove);
    }

    @DeleteMapping(value = "/items/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public void clearCart() {
        User user = userService.getCurrentUser();
        RepairCart cart = user.getRepairCart();
        repairCartService.removeAll(cart);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RepairCart getRepairCartById(@PathVariable Integer id) {
        User user = userService.findUser(id);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User with id " + id + " is not found" );
        }
        return user.getRepairCart();
    }
}
