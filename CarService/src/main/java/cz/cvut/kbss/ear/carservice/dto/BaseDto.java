package cz.cvut.kbss.ear.carservice.dto;

import java.io.Serializable;

public class BaseDto implements Serializable {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
