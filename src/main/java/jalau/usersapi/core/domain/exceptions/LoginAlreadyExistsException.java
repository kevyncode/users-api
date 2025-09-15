package jalau.usersapi.core.domain.exceptions;

/**
 * Exception thrown when attempting to create a user with a login that already exists
 * 
 * This exception represents a business rule violation in the domain layer
 * and should be handled appropriately by the presentation layer.
 */
public class LoginAlreadyExistsException extends RuntimeException {
    
    /**
     * Creates a new LoginAlreadyExistsException with a specific message
     * 
     * @param login the login that already exists
     */
    public LoginAlreadyExistsException(String login) {
        super(String.format("User with login '%s' already exists", login));
    }
    
    /**
     * Creates a new LoginAlreadyExistsException with a custom message
     * 
     * @param message the custom error message
     */
    public LoginAlreadyExistsException(String login, String message) {
        super(message);
    }
}