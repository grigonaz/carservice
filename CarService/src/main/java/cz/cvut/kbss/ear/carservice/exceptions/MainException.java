package cz.cvut.kbss.ear.carservice.exceptions;

public class MainException extends RuntimeException {
    public MainException() {
    }

    public MainException(String message) {
        super(message);
    }

    public MainException(String message, Throwable cause) {
        super(message, cause);
    }

    public MainException(Throwable cause) {
        super(cause);
    }
}
