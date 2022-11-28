package cz.cvut.kbss.ear.carservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class WorkDto {
    @JsonFormat(locale = "hu", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Budapest")
    private LocalDateTime beginWork;

    @JsonFormat(locale = "hu", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Budapest")
    private LocalDateTime finishWork;

    private Integer repairId;

    public LocalDateTime getBeginWork() {
        return beginWork;
    }

    public void setBeginWork(LocalDateTime beginWork) {
        this.beginWork = beginWork;
    }

    public LocalDateTime getFinishWork() {
        return finishWork;
    }

    public void setFinishWork(LocalDateTime finishWork) {
        this.finishWork = finishWork;
    }

    public Integer getRepairId() {
        return repairId;
    }

    public void setRepairId(Integer repairId) {
        this.repairId = repairId;
    }
}
