package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.exceptions.PermissionException;
import cz.cvut.kbss.ear.carservice.model.Notification;
import cz.cvut.kbss.ear.carservice.model.Role;
import cz.cvut.kbss.ear.carservice.model.User;
import cz.cvut.kbss.ear.carservice.service.NotificationService;
import cz.cvut.kbss.ear.carservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/notifications")
@PreAuthorize("permitAll()")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Notification> getNotifications() {
        return notificationService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{username}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Notification> getNotificationsByUsername(@PathVariable String username) {
        List<Notification> nots = notificationService.findAllByUserName(username);
        if (nots == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Username with id " + username + " is not found");
        }
        return nots;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Notification getById( @PathVariable Integer id) {
        final Notification notification = notificationService.find(id);
        if (notification == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id " + id + " is not found");
        }
        User user = userService.getCurrentUser();
        if (user.getRole() != Role.ADMIN &&
                !notification.getUser().getId().equals(user.getId())) {
            throw new PermissionException("Cannot notification order of another customer");
        }
        return notification;
    }
}
