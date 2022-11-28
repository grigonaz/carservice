package cz.cvut.kbss.ear.carservice.model;

import javax.persistence.*;

@Entity
@Table(name = "serviceitem")
public class ServiceItem extends Abstract{

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AutoService autoService;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public AutoService getAutoService() {
        return autoService;
    }

    public void setAutoService(AutoService autoService) {
        this.autoService = autoService;
    }
}
