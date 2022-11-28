package cz.cvut.kbss.ear.carservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.kbss.ear.carservice.dto.CarDto;
import cz.cvut.kbss.ear.carservice.exceptions.EmptyCartException;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "repair")
@NamedQueries({
        @NamedQuery(name = "Repair.findAllNotDone", query = "SELECT r FROM Repair r WHERE r.repairStatus <> 'DONE'")
})
public class Repair extends Abstract {

    @Basic(optional = false)
    @Column(nullable = false)
    private String car;

    @Enumerated(EnumType.STRING)
    private RepairStatus repairStatus;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User client;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "repair_id")
    private List<RepairItem> items;

    @OneToMany
    @JoinColumn(nullable = false)
    private List<Work> works;

    public Repair() {
    }

    public Repair(RepairCart repairCart, CarDto carDto) {
        if (repairCart.getItems().isEmpty()) {
            throw new EmptyCartException("Attempt to create Repair with empty RepairService");
        }
        this.car = carDto.getName();
        this.client = repairCart.getOwner();
        this.repairStatus = RepairStatus.WAITING;
        this.items = repairCart.getItems().stream().map(RepairItem::new).collect(Collectors.toList());

        repairCart.getItems().clear();
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public RepairStatus getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(RepairStatus repairStatus) {
        this.repairStatus = repairStatus;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public List<RepairItem> getServices() {
        return items;
    }

    public void setServices(List<RepairItem> services) {
        this.items = services;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User user) {
        this.client = user;
    }
}
