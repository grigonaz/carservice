package cz.cvut.kbss.ear.carservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification extends Abstract {

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message;

    private LocalDateTime created;

    @JsonIgnore
    @ManyToOne
    private User user;

    public Notification() {
    }

    public Notification(Repair repair) {
        this.type = NotificationType.REPAIR;
        this.user =  repair.getClient();
        this.created = LocalDateTime.now();
        this.message = "Your status of services with id "+ repair.getId() +
                " has changed to '"+ repair.getRepairStatus().toString().toLowerCase()+"'";
    }

    public Notification(Order order) {
        this.type = NotificationType.ORDER;
        this.user = order.getClient();
        this.created = LocalDateTime.now();
        this.message = "Your status of "+ order.getClass().getSimpleName().toLowerCase()
                +" with id "+ order.getId() +" has changed to '"+ order.getInfo().toString().toLowerCase()+"'";
    }


    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
