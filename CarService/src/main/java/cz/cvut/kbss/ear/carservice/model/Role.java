package cz.cvut.kbss.ear.carservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum Role {
    ADMIN("ROLE_ADMIN"), EMPLOYEE("ROLE_EMPLOYEE"), CLIENT("ROLE_CLIENT");

    private final String name;

    Role (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

