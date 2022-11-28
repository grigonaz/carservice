package cz.cvut.kbss.ear.carservice.exceptions;

public class LackOfFundsException extends MainException {
    public LackOfFundsException(String message) {
        super(message);
    }

    public LackOfFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LackOfFundsException(Throwable cause) {
        super(cause);
    }
}
