package jalau.usersapi.core.domain.exceptions;

/**
 * Exception thrown when attempting to access a user that does not exist
 *
 * This exception represents a business rule violation in the domain layer
 * and should be handled appropriately by the presentation layer.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Creates a new UserNotFoundException with a specific message
     *
     * @param id the user ID that was not found
     */
    public UserNotFoundException(String id) {
        super(String.format("User with ID '%s' not found", id));
    }

    /**
     * Creates a new UserNotFoundException with a custom message
     *
     * @param id the user ID that was not found
     * @param message the custom error message
     */
    public UserNotFoundException(String id, String message) {
        super(message);
    }
}