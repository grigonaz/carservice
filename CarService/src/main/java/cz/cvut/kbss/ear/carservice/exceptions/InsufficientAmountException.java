package cz.cvut.kbss.ear.carservice.exceptions;

public class InsufficientAmountException extends MainException {
    public InsufficientAmountException(String message) {
        super(message);
    }

    public InsufficientAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientAmountException(Throwable cause) {
        super(cause);
    }
}
