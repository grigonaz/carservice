package cz.cvut.kbss.ear.carservice.model;

public enum RepairStatus {
    WAITING("WAITING"), INPROGRESS("NPROGRESS"), DONE("DONE");

    private final String name;

    RepairStatus (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
