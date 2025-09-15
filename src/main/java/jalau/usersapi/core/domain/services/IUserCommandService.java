package jalau.usersapi.core.domain.services;

import jalau.usersapi.core.domain.entities.User;

/**
 * Interface for user command operations
 * 
 * This interface defines the contract for user command operations
 * (create, update, delete) in the domain layer following Clean Architecture principles.
 */
public interface IUserCommandService {
    
    /**
     * Creates a new user in the system
     * 
     * @param user the user to create (must have null id)
     * @return the created user with non-null id
     */
    User createUser(User user);
    
    /**
     * Updates an existing user in the system
     * 
     * @param user the user to update (must have non-null id)
     * @return the updated user with non-null id
     */
    User updateUser(User user);
    
    /**
     * Deletes a user from the system
     * 
     * @param id the unique identifier of the user to delete
     */
    void deleteUser(String id);
}
