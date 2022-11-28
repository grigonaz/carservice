package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.dto.WorkDto;
import cz.cvut.kbss.ear.carservice.model.*;
import cz.cvut.kbss.ear.carservice.rest.util.RestUtils;
import cz.cvut.kbss.ear.carservice.service.RepairService;
import cz.cvut.kbss.ear.carservice.service.UserService;
import cz.cvut.kbss.ear.carservice.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/work")
public class WorkController {

    private final WorkService workService;
    private final UserService userService;
    private final RepairService repairService;

    @Autowired
    public WorkController(WorkService workService, UserService userService, RepairService repairService) {
        this.workService = workService;
        this.userService = userService;
        this.repairService = repairService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') ")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWork(@RequestBody WorkDto workDto) {
        Work work = new Work();
        work.setFinishWork(workDto.getFinishWork());
        work.setRepair(repairService.findById(workDto.getRepairId()));
        work.setBeginWork(workDto.getBeginWork());
        workService.create(work);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", work.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_EMPLOYEE')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Work getWork(@PathVariable Integer id) {
        final Work work = workService.findById(id);
        if (work == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Work with id " + id + " is not found" );
        }
        User user = userService.getCurrentUser();
        return work;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_EMPLOYEE')")
    @GetMapping(value = "/{id}/all",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Work> getAllByEmployeeId(@PathVariable Integer id) {
        return workService.findAllByEmployeeId(id);
    }
}
