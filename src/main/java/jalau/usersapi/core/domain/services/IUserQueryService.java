package jalau.usersapi.core.domain.services;

import jalau.usersapi.core.domain.entities.User;
import java.util.List;

/**
 * Interface for user query operations
 * 
 * This interface defines the contract for user query operations
 * in the domain layer following Clean Architecture principles.
 */
public interface IUserQueryService {
    
    /**
     * Retrieves all users from the system
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
}
