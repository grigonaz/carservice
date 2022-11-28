package cz.cvut.kbss.ear.carservice.dto;

import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.RepairStatus;

public class RepairStatusDto {

    String repairStatusStr;
    RepairStatus info;

    public RepairStatus getInfo() {
        return info;
    }

    public void setInfo(String repairStatusStr) {
        switch (repairStatusStr) {
            case "WAITING":
                this.info = RepairStatus.WAITING;
                break;
            case "INPROGRESS":
                this.info = RepairStatus.INPROGRESS;
                break;
            case "DONE":
                this.info = RepairStatus.DONE;
                break;
            default:
                throw new NotFoundException("Status with name " + repairStatusStr + " doesn't exist");
        }
    }
}
