package cz.cvut.kbss.ear.carservice.rest.util;

import cz.cvut.kbss.ear.carservice.model.Role;

public final class Constants {

    /**
     * Default user role
     */
    public static final Role DEFAULT_ROLE = Role.CLIENT;

    private Constants() {
        throw new AssertionError();
    }
}
