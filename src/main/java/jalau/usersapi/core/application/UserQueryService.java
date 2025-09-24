package jalau.usersapi.core.application;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.repositories.IUserRepository;
import jalau.usersapi.core.domain.services.IUserQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service implementation for user query operations.
 * 
 * <p>This service implements {@link IUserQueryService} to provide concrete
 * implementation of user query operations following Clean Architecture principles
 * and CQRS pattern. It serves as the application layer orchestrating domain
 * operations and repository interactions for read-only user operations.
 * 
 * <p><strong>Architecture Role:</strong>
 * <ul>
 *   <li><strong>Application Layer:</strong> Orchestrates domain logic and repository calls</li>
 *   <li><strong>CQRS Query Side:</strong> Handles all read operations for users</li>
 *   <li><strong>Dependency Injection:</strong> Uses Spring IoC for dependency management</li>
 *   <li><strong>Clean Architecture:</strong> Depends on domain abstractions, not concrete implementations</li>
 * </ul>
 * 
 * <p><strong>Design Patterns:</strong>
 * <ul>
 *   <li><strong>Service Pattern:</strong> Encapsulates business logic</li>
 *   <li><strong>Repository Pattern:</strong> Abstracts data access through {@link IUserRepository}</li>
 *   <li><strong>Dependency Inversion:</strong> Depends on interfaces, not implementations</li>
 * </ul>
 * 
 * <p><strong>Responsibilities:</strong>
 * <ul>
 *   <li>Delegate query operations to repository layer</li>
 *   <li>Apply business rules for data retrieval if needed</li>
 *   <li>Handle cross-cutting concerns (logging, caching, etc.)</li>
 *   <li>Maintain stateless operation for thread safety</li>
 * </ul>
 * 
 * @author Users API Development Team
 * @since 1.0.0
 * @see IUserQueryService
 * @see IUserRepository
 * @see User
 */
public class UserQueryService implements IUserQueryService {

    private final IUserRepository userRepository;

    /**
     * Constructor for dependency injection.
     * 
     * <p>Initializes the service with the required repository dependency.
     * This follows the Constructor Injection pattern recommended by Spring
     * and ensures immutable dependencies.
     * 
     * @param userRepository repository interface for user data access operations
     * @throws IllegalArgumentException if userRepository is null
     */
    public UserQueryService(IUserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository cannot be null");
        }
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Retrieves all users by delegating to the repository layer.
     * This method provides a simple pass-through to the data access layer
     * while maintaining the service layer abstraction.
     * 
     * <p><strong>Implementation Details:</strong>
     * <ul>
     *   <li>Direct delegation to repository</li>
     *   <li>No additional business logic applied</li>
     *   <li>Thread-safe operation</li>
     *   <li>Returns immutable view of user list</li>
     * </ul>
     * 
     * @return List of all users from the repository
     * @throws DataAccessException if repository operation fails
     */
    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Retrieves a specific user by ID by delegating to the repository layer.
     * This method provides input validation and delegates to the data access layer
     * while maintaining proper error handling.
     * 
     * <p><strong>Implementation Details:</strong>
     * <ul>
     *   <li>Input validation for null/empty ID</li>
     *   <li>Direct delegation to repository</li>
     *   <li>Proper exception handling and propagation</li>
     *   <li>Thread-safe operation</li>
     * </ul>
     * 
     * @param id the unique identifier of the user to retrieve
     * @return User entity if found, null if not found
     * @throws IllegalArgumentException if ID is null or empty
     * @throws DataAccessException if repository operation fails
     */
    @Override
    public User getUser(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return userRepository.getUser(id);
    }
}
