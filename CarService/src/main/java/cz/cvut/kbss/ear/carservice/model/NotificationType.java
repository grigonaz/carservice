package cz.cvut.kbss.ear.carservice.model;

public enum NotificationType {
    ORDER("TYPE_ORDER"), REPAIR("TYPE_REPAIR");

    private final String name;

    NotificationType (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
