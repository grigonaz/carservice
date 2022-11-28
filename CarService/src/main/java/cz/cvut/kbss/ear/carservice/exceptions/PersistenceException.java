package cz.cvut.kbss.ear.carservice.exceptions;

public class PersistenceException extends MainException {
    public PersistenceException() {
        super();
    }
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
