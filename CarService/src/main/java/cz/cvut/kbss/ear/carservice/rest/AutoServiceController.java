package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.AutoService;
import cz.cvut.kbss.ear.carservice.rest.util.RestUtils;
import cz.cvut.kbss.ear.carservice.service.AutoServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/autoservice")
public class AutoServiceController {

    private final AutoServiceService autoServiceService;

    @Autowired
    public AutoServiceController(AutoServiceService autoServiceService) {
        this.autoServiceService = autoServiceService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AutoService> getAutoServices() {
        return autoServiceService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createAutoService(@RequestBody AutoService autoService) {
        autoServiceService.createAutoService(autoService);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", autoService.getId());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AutoService getAutoService(@PathVariable Integer id) {
        final AutoService autoService = autoServiceService.findById(id);

        if (autoService == null) {
            throw NotFoundException.create("AutoService", id);
        }

        return autoService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateAutoService(@PathVariable Integer id, @RequestBody AutoService autoService) {
        final AutoService got = getAutoService(id);

        if (!got.getId().equals(autoService.getId())) {
            throw new InvalidDataException("AutoService identifier in the data does not match the one in the request URL.");
        }
        autoServiceService.update(autoService);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @DeleteMapping(value = "/{id}")
    public void deactivateAutoService(@PathVariable Integer id) {
        final AutoService autoService = autoServiceService.findById(id);
        if (autoService == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "AutoService with id " + id + " is not found" );
        }
        autoServiceService.deactivate(autoService);
    }
}
