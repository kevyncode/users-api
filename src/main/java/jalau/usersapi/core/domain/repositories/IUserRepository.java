package jalau.usersapi.core.domain.repositories;

import jalau.usersapi.core.domain.entities.User;
import java.util.List;

/**
 * Repository interface for user persistence operations
 * 
 * This interface defines the contract for user data access operations
 * in the domain layer following Clean Architecture and DDD principles.
 */
public interface IUserRepository {
    
    /**
     * Creates a new user in the persistence layer
     * 
     * @param user the user to create
     * @return the created user with assigned id
     */
    User createUser(User user);
    
    /**
     * Updates an existing user in the persistence layer
     * 
     * @param user the user to update
     * @return the updated user
     */
    User updateUser(User user);
    
    /**
     * Deletes a user from the persistence layer
     * 
     * @param id the unique identifier of the user to delete
     */
    void deleteUser(String id);
    
    /**
     * Retrieves all users from the persistence layer
     * 
     * @return List of all users
     */
    List<User> getUsers();
    
    /**
     * Retrieves a single user by their unique identifier
     * 
     * @param id the user's unique identifier
     * @return the user with the specified id, or null if not found
     */
    User getUser(String id);
    
    /**
     * Checks if a user with the specified login already exists
     * 
     * @param login the login to check
     * @return true if a user with this login exists, false otherwise
     */
    boolean existsByLogin(String login);
}
