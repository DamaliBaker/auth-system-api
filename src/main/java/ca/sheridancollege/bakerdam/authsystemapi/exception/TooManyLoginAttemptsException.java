package ca.sheridancollege.bakerdam.authsystemapi.exception;

public class TooManyLoginAttemptsException extends RuntimeException {
    public TooManyLoginAttemptsException() {
        super("Too many failed login attempts. Please try again later.");
    }
}