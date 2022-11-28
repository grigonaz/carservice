package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.dto.CarDto;
import cz.cvut.kbss.ear.carservice.dto.RepairStatusDto;
import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.*;
import cz.cvut.kbss.ear.carservice.rest.util.RestUtils;
import cz.cvut.kbss.ear.carservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/repairs")
public class RepairController {

    private final RepairService repairService;
    private final UserService userService;

    @Autowired
    public RepairController(RepairService repairService, UserService userService) {
        this.repairService = repairService;
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createRepair(@RequestBody CarDto carDto) {
        RepairCart cart = userService.getCurrentUser().getRepairCart();
        final Repair repair = repairService.createRepair(cart,carDto);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", repair.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_EMPLOYEE')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Repair getRepair(@PathVariable Integer id) {
        final Repair repair = repairService.findById(id);
        if (repair == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Repair with id " + id + " is not found" );
        }
        User user = userService.getCurrentUser();
        if ((user.getRole() != Role.ADMIN || user.getRole() != Role.EMPLOYEE) && !repair.getClient().getId().equals(user.getId())) {
            throw new AccessDeniedException("Cannot access order of another customer");
        }
        return repair;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeStatus(@PathVariable Integer id, @RequestBody RepairStatusDto status) {
        Repair repairToChange = repairService.findById(id);
        if (repairToChange == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Repair with id " + id + " is not found" );
        }
        repairService.changeStatus(repairToChange, status.getInfo());
    }
}
