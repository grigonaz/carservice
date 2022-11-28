package cz.cvut.kbss.ear.carservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "work")
public class Work extends Abstract {

    @JsonFormat(locale = "hu", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Budapest")
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime beginWork;

    @JsonFormat(locale = "hu", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Budapest")
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime finishWork;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Repair repair;

    @JsonIgnore
    @ManyToOne
    private User employee;

    public Work() {
    }

    public Work(Repair repair, LocalDateTime start, LocalDateTime end) {
        this.repair = repair;
        this.beginWork = start;
        this.finishWork = end;
    }

    public static Work create(Repair repair, LocalDateTime start, LocalDateTime end) {
        return new Work(repair,start,end);
    }

    public LocalDateTime getBeginWork() {
        return beginWork;
    }

    public void setBeginWork(LocalDateTime start) {
        this.beginWork = start;
    }

    public LocalDateTime getFinishWork() {
        return finishWork;
    }

    public void setFinishWork(LocalDateTime end) {
        this.finishWork = end;
    }

    public Repair getRepair() {
        return repair;
    }

    public void setRepair(Repair repair) {
        this.repair = repair;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}
