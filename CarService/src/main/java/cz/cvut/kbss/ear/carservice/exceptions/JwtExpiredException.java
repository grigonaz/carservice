package cz.cvut.kbss.ear.carservice.exceptions;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException() {
        super("Session time expired");
    }
}
