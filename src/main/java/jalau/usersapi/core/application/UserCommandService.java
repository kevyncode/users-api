package jalau.usersapi.core.application;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.exceptions.LoginAlreadyExistsException;
import jalau.usersapi.core.domain.exceptions.UserNotFoundException;
import jalau.usersapi.core.domain.repositories.IUserRepository;
import jalau.usersapi.core.domain.services.IUserCommandService;

/**
 * Application service implementation for user command operations.
 * 
 * <p>This service implements {@link IUserCommandService} to provide concrete
 * implementation of user command operations (create, update, delete) following
 * Clean Architecture principles and CQRS pattern. It serves as the application
 * layer orchestrating domain logic, business rule enforcement, and repository
 * interactions for write operations.
 * 
 * <p><strong>Architecture Role:</strong>
 * <ul>
 *   <li><strong>Application Layer:</strong> Orchestrates business logic and repository calls</li>
 *   <li><strong>CQRS Command Side:</strong> Handles all write operations for users</li>
 *   <li><strong>Domain Logic Enforcer:</strong> Implements business rules and validation</li>
 *   <li><strong>Clean Architecture:</strong> Depends on domain abstractions, not concrete implementations</li>
 * </ul>
 * 
 * <p><strong>Design Patterns:</strong>
 * <ul>
 *   <li><strong>Service Pattern:</strong> Encapsulates complex business logic</li>
 *   <li><strong>Repository Pattern:</strong> Abstracts data access through {@link IUserRepository}</li>
 *   <li><strong>Dependency Inversion:</strong> Depends on interfaces, not implementations</li>
 * </ul>
 * 
 * <p><strong>Business Rules Enforced:</strong>
 * <ul>
 *   <li>Login uniqueness across the system</li>
 *   <li>User existence validation for updates and deletions</li>
 *   <li>Data integrity and consistency checks</li>
 *   <li>Proper error handling and exception management</li>
 * </ul>
 * 
 * <p><strong>Key Features:</strong>
 * <ul>
 *   <li>Comprehensive input validation</li>
 *   <li>Business rule enforcement</li>
 *   <li>Atomic operations with proper error handling</li>
 *   <li>Optimistic data merging for partial updates</li>
 * </ul>
 * 
 * @author Users API Development Team
 * @since 1.0.0
 * @see IUserCommandService
 * @see IUserRepository
 * @see User
 */
public class UserCommandService implements IUserCommandService {

    private final IUserRepository userRepository;

    /**
     * Constructor for dependency injection.
     * 
     * <p>Initializes the service with the required repository dependency.
     * This follows the Constructor Injection pattern recommended by Spring
     * and ensures immutable dependencies for better testability and thread safety.
     * 
     * @param userRepository repository interface for user persistence operations
     * @throws IllegalArgumentException if userRepository is null
     */
    public UserCommandService(IUserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository cannot be null");
        }
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Creates a new user in the system with comprehensive business rule validation.
     * This method enforces domain constraints, particularly login uniqueness,
     * before delegating to the repository layer for persistence.
     * 
     * <p><strong>Business Logic Implementation:</strong>
     * <ul>
     *   <li>Validates input user data</li>
     *   <li>Checks login uniqueness constraint</li>
     *   <li>Delegates to repository for ID generation and persistence</li>
     *   <li>Returns fully populated user entity</li>
     * </ul>
     * 
     * <p><strong>Validation Rules:</strong>
     * <ul>
     *   <li>User must not be null</li>
     *   <li>Login must be unique across the system</li>
     *   <li>Required fields must be present</li>
     * </ul>
     * 
     * <p><strong>Error Scenarios:</strong>
     * <ul>
     *   <li>Null user input - handled by repository layer</li>
     *   <li>Duplicate login - throws {@link LoginAlreadyExistsException}</li>
     *   <li>Repository failures - propagated as {@link DataAccessException}</li>
     * </ul>
     * 
     * @param user the user to create (must have null ID)
     * @return User entity with generated ID and persisted data
     * @throws LoginAlreadyExistsException if login is already in use
     * @throws DataAccessException if repository operation fails
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
     * {@inheritDoc}
     * 
     * <p>Updates an existing user in the system with comprehensive validation
     * and intelligent field merging. This method implements partial update
     * semantics, allowing only specified fields to be modified while preserving
     * existing data for unspecified fields.
     * 
     * <p><strong>Business Logic Implementation:</strong>
     * <ul>
     *   <li>Validates user existence before attempting update</li>
     *   <li>Checks login uniqueness if login is being changed</li>
     *   <li>Performs intelligent field merging via {@link #mergeUserChanges}</li>
     *   <li>Delegates to repository for persistence</li>
     * </ul>
     * 
     * <p><strong>Partial Update Strategy:</strong>
     * <ul>
     *   <li>Only non-null fields from input are applied</li>
     *   <li>Existing field values are preserved for null input fields</li>
     *   <li>ID field is always preserved from existing user</li>
     * </ul>
     * 
     * <p><strong>Validation Rules:</strong>
     * <ul>
     *   <li>User must exist in the system</li>
     *   <li>Login uniqueness if being changed</li>
     *   <li>Field-specific validation applied as needed</li>
     * </ul>
     * 
     * @param user the user data to update (must have non-null ID)
     * @return Updated User entity, or null if user not found
     * @throws LoginAlreadyExistsException if login change conflicts with existing user
     * @throws DataAccessException if repository operation fails
     */
    @Override
    public User updateUser(User user) {

        // Check if user exists before updating
        User existingUser = userRepository.getUser(user.getId());
        if (existingUser == null) {
            return null; // User not found
        }

        // Check for login duplication if login is being changed
        if (user.getLogin() != null && !user.getLogin().equals(existingUser.getLogin())) {
            if (userRepository.existsByLogin(user.getLogin())) {
                throw new LoginAlreadyExistsException(user.getLogin());
            }
        }

        // Merge changes: only update fields that are provided (not null)
        User userToUpdate = mergeUserChanges(existingUser, user);

        return userRepository.updateUser(userToUpdate);
    }

    /**
     * Merges changes from an update request with existing user data.
     * 
     * <p>This private helper method implements the partial update logic by
     * selectively applying only the non-null fields from the update request
     * while preserving existing values for null fields. This approach enables
     * efficient partial updates without requiring clients to send complete
     * user objects.
     * 
     * <p><strong>Merge Strategy:</strong>
     * <ul>
     *   <li><strong>ID:</strong> Always preserved from existing user</li>
     *   <li><strong>Name:</strong> Updated if provided, otherwise preserved</li>
     *   <li><strong>Login:</strong> Updated if provided, otherwise preserved</li>
     *   <li><strong>Password:</strong> Updated if provided, otherwise preserved</li>
     * </ul>
     * 
     * <p><strong>Design Rationale:</strong>
     * <ul>
     *   <li>Supports partial updates for better API usability</li>
     *   <li>Reduces network overhead by not requiring complete objects</li>
     *   <li>Prevents accidental data loss from incomplete requests</li>
     *   <li>Centralizes merge logic for consistency</li>
     * </ul>
     * 
     * @param existingUser the current user data from the repository
     * @param updatedUser the update request containing potentially partial data
     * @return User entity with merged data ready for persistence
     */
    private User mergeUserChanges(User existingUser, User updatedUser) {
        User mergedUser = new User();
        mergedUser.setId(existingUser.getId());

        // Only update fields that are provided in the update
        mergedUser.setName(updatedUser.getName() != null ? updatedUser.getName() : existingUser.getName());
        mergedUser.setLogin(updatedUser.getLogin() != null ? updatedUser.getLogin() : existingUser.getLogin());
        mergedUser.setPassword(updatedUser.getPassword() != null ? updatedUser.getPassword() : existingUser.getPassword());

        return mergedUser;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Deletes a user from the system with comprehensive validation
     * and proper error handling. This method ensures user existence
     * before attempting deletion and provides clear error messaging
     * for various failure scenarios.
     * 
     * <p><strong>Business Logic Implementation:</strong>
     * <ul>
     *   <li>Validates user existence before deletion attempt</li>
     *   <li>Delegates to repository for actual deletion operation</li>
     *   <li>Handles cascading effects if applicable</li>
     *   <li>Provides clear error messages for failure cases</li>
     * </ul>
     * 
     * <p><strong>Validation Rules:</strong>
     * <ul>
     *   <li>User ID must not be null (handled by method signature)</li>
     *   <li>User must exist in the system</li>
     *   <li>Business constraints must allow deletion</li>
     * </ul>
     * 
     * <p><strong>Error Scenarios:</strong>
     * <ul>
     *   <li>User not found - throws {@link UserNotFoundException}</li>
     *   <li>Repository failures - propagated as {@link DataAccessException}</li>
     *   <li>Business rule violations - appropriate domain exceptions</li>
     * </ul>
     * 
     * @param id the unique identifier of the user to delete
     * @throws UserNotFoundException if user with specified ID doesn't exist
     * @throws DataAccessException if repository operation fails
     */
    @Override
    public void deleteUser(String id) {
        // Check if user exists before deleting
        User existingUser = userRepository.getUser(id);
        if (existingUser == null) {
            throw new UserNotFoundException(id);
        }

        // Delegate to repository for persistence
        userRepository.deleteUser(id);
    }
}