package cz.cvut.kbss.ear.carservice.model;

public enum OrderItemInfo {
    ORDERED("DETAIL_ORDERED"), ONTHEWAY("DETAIL_ONTHEWAY"), DELIVERED("DETAIL_DELIVERED");

    private final String name;

    OrderItemInfo(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
