package jalau.usersapi.core.application;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.exceptions.LoginAlreadyExistsException;
import jalau.usersapi.core.domain.repositories.IUserRepository;
import jalau.usersapi.core.domain.services.IUserCommandService;

/**
 * Service implementation for user command operations
 * 
 * This service implements the business logic for user command operations
 * following the Clean Architecture pattern.
 */
public class UserCommandService implements IUserCommandService {
    
    private final IUserRepository userRepository;
    
    /**
     * Constructor for dependency injection
     * 
     * @param userRepository repository for user persistence operations
     */
    public UserCommandService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Creates a new user in the system
     * 
     * @param user the user to create (must have null id)
     * @return the created user with non-null id
     * @throws LoginAlreadyExistsException if login already exists
     */
    @Override
    public User createUser(User user) {
        // Validate input - let null users pass through to maintain existing behavior
        if (user != null && user.getLogin() != null) {
            // Check if login already exists
            if (userRepository.existsByLogin(user.getLogin())) {
                throw new LoginAlreadyExistsException(user.getLogin());
            }
        }
        
        // Business logic can be added here in future user stories
        // Delegate to repository for persistence
        return userRepository.createUser(user);
    }
    
    /**
     * Updates an existing user in the system
     * 
     * @param user the user to update (must have non-null id)
     * @return the updated user with non-null id
     */
    @Override
    public User updateUser(User user) {
        throw new RuntimeException("Not implemented yet");
    }
    
    /**
     * Deletes a user from the system
     * 
     * @param id the unique identifier of the user to delete
     */
    @Override
    public void deleteUser(String id) {
        throw new RuntimeException("Not implemented yet");
    }
}
