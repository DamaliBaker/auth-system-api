package ca.sheridancollege.bakerdam.authsystemapi.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User ID #" + id + " not found");
    }

    public UserNotFoundException(String email) {
        super("User with email '" + email + "' not found");
    }
}
