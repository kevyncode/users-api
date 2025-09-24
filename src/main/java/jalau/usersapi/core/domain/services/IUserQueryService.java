package jalau.usersapi.core.domain.services;

import jalau.usersapi.core.domain.entities.User;
import java.util.List;

/**
 * Service interface for user query operations in the domain layer.
 * 
 * <p>This interface defines the contract for user query operations following
 * Clean Architecture principles and CQRS (Command Query Responsibility Segregation) pattern.
 * It separates read operations from write operations to optimize performance and
 * maintain clear separation of concerns.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li><strong>Domain Layer:</strong> Contains core business logic and rules</li>
 *   <li><strong>CQRS Pattern:</strong> Dedicated to query (read) operations only</li>
 *   <li><strong>Dependency Inversion:</strong> Implementation details are abstracted</li>
 *   <li><strong>Single Responsibility:</strong> Focused solely on user data retrieval</li>
 * </ul>
 * 
 * <p><strong>Implementation Notes:</strong>
 * <ul>
 *   <li>Implementations should be stateless and thread-safe</li>
 *   <li>All operations are read-only and should not modify data</li>
 *   <li>Should delegate to repository layer for data access</li>
 *   <li>Business validation logic should be applied where necessary</li>
 * </ul>
 * 
 * @author Users API Development Team
 * @since 1.0.0
 * @see User
 * @see IUserCommandService
 */
public interface IUserQueryService {
    
    /**
     * Retrieves all users from the system.
     * 
     * <p>This method returns a complete list of all users currently stored
     * in the system. The operation is optimized for read performance and
     * follows CQRS query principles.
     * 
     * <p><strong>Business Rules:</strong>
     * <ul>
     *   <li>Returns empty list if no users exist</li>
     *   <li>Results are not filtered or paginated</li>
     *   <li>No authentication required at service level</li>
     * </ul>
     * 
     * @return List&lt;User&gt; containing all users in the system, 
     *         empty list if no users exist
     * @throws DataAccessException if database access fails
     * @see User
     */
    List<User> getUsers();
    
    /**
     * Retrieves a single user by their unique identifier.
     * 
     * <p>This method performs a lookup operation to find a specific user
     * based on their unique ID. The operation follows domain-driven design
     * principles and includes proper error handling for non-existent users.
     * 
     * <p><strong>Business Rules:</strong>
     * <ul>
     *   <li>ID must be in valid UUID format</li>
     *   <li>Returns null if user is not found</li>
     *   <li>No side effects on system state</li>
     * </ul>
     * 
     * @param id the user's unique identifier (UUID format expected)
     * @return User entity if found, null otherwise
     * @throws IllegalArgumentException if ID is null or invalid format
     * @throws DataAccessException if database access fails
     * @see User
     */
    User getUser(String id);
}
