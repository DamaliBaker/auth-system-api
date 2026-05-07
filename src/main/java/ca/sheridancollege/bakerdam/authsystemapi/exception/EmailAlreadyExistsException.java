package ca.sheridancollege.bakerdam.authsystemapi.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email '" + email + "' is already taken.");
    }
}
