package cz.cvut.kbss.ear.carservice.exceptions;

public class ItemNotExistException extends MainException {
    public ItemNotExistException(String message) {
        super(message);
    }

    public ItemNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
