package cz.cvut.kbss.ear.carservice.exceptions;

public class EmptyCartException extends MainException {
    public EmptyCartException(String message) {
        super(message);
    }

    public EmptyCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyCartException(Throwable cause) {
        super(cause);
    }
}
