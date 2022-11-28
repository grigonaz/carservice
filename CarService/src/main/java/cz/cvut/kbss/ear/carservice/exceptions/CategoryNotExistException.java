package cz.cvut.kbss.ear.carservice.exceptions;

public class CategoryNotExistException extends MainException {
    public CategoryNotExistException(String message) {
        super(message);
    }

    public CategoryNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryNotExistException(Throwable cause) {
        super(cause);
    }
}
