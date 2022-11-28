package cz.cvut.kbss.ear.carservice.model;

public enum OrderInfo {
    ORDERED("ORDERED"), ONTHEWAY("ON THE WAY"), DELIVERED("DELIVERED");

    private final String name;

    OrderInfo (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
