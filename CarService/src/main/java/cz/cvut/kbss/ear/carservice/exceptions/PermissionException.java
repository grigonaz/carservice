package cz.cvut.kbss.ear.carservice.exceptions;

public class PermissionException extends MainException {
    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
