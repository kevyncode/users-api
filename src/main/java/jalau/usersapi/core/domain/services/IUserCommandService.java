package jalau.usersapi.core.domain.services;

import jalau.usersapi.core.domain.entities.User;

/**
 * Service interface for user command operations in the domain layer.
 * 
 * <p>This interface defines the contract for user command operations (create, update, delete)
 * following Clean Architecture principles and CQRS (Command Query Responsibility Segregation) pattern.
 * It separates write operations from read operations to optimize performance and
 * maintain clear separation of concerns.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li><strong>Domain Layer:</strong> Contains core business logic and validation rules</li>
 *   <li><strong>CQRS Pattern:</strong> Dedicated to command (write) operations only</li>
 *   <li><strong>Dependency Inversion:</strong> Implementation details are abstracted</li>
 *   <li><strong>Single Responsibility:</strong> Focused solely on user data modification</li>
 * </ul>
 * 
 * <p><strong>Implementation Notes:</strong>
 * <ul>
 *   <li>Implementations should be stateless and thread-safe</li>
 *   <li>All operations modify system state and should be transactional</li>
 *   <li>Should delegate to repository layer for data persistence</li>
 *   <li>Business validation and domain rules must be enforced</li>
 *   <li>Should handle concurrency and data consistency issues</li>
 * </ul>
 * 
 * <p><strong>Error Handling:</strong>
 * <ul>
 *   <li>Validation errors should throw appropriate domain exceptions</li>
 *   <li>Business rule violations should be clearly communicated</li>
 *   <li>Data access issues should be properly handled and logged</li>
 * </ul>
 * 
 * @author Users API Development Team
 * @since 1.0.0
 * @see User
 * @see IUserQueryService
 */
public interface IUserCommandService {
    
    /**
     * Creates a new user in the system.
     * 
     * <p>This method handles the creation of a new user with comprehensive
     * business rule validation and data persistence. The operation includes
     * unique constraint validation and proper ID generation.
     * 
     * <p><strong>Business Rules:</strong>
     * <ul>
     *   <li>User ID must be null (will be auto-generated)</li>
     *   <li>Login must be unique across the system</li>
     *   <li>Name cannot be null or empty</li>
     *   <li>Password must meet security requirements</li>
     * </ul>
     * 
     * <p><strong>Process Flow:</strong>
     * <ol>
     *   <li>Validate input data and business rules</li>
     *   <li>Check login uniqueness</li>
     *   <li>Generate unique UUID for the user</li>
     *   <li>Persist user to repository</li>
     *   <li>Return created user with generated ID</li>
     * </ol>
     * 
     * @param user the user to create (must have null ID)
     * @return User entity with generated ID and persisted data
     * @throws IllegalArgumentException if user is null or has non-null ID
     * @throws LoginAlreadyExistsException if login is already in use
     * @throws ValidationException if user data fails business validation
     * @throws DataAccessException if persistence operation fails
     * @see User
     */
    User createUser(User user);
    
    /**
     * Updates an existing user in the system.
     * 
     * <p>This method handles the modification of existing user data with
     * comprehensive validation and business rule enforcement. The operation
     * includes existence verification and constraint validation.
     * 
     * <p><strong>Business Rules:</strong>
     * <ul>
     *   <li>User ID must not be null and must exist in the system</li>
     *   <li>Login must be unique if being changed</li>
     *   <li>Name cannot be null or empty</li>
     *   <li>Password updates must meet security requirements</li>
     * </ul>
     * 
     * <p><strong>Process Flow:</strong>
     * <ol>
     *   <li>Validate input data and business rules</li>
     *   <li>Verify user existence</li>
     *   <li>Check login uniqueness if changed</li>
     *   <li>Apply updates to user data</li>
     *   <li>Persist changes to repository</li>
     *   <li>Return updated user entity</li>
     * </ol>
     * 
     * @param user the user to update (must have non-null ID)
     * @return User entity with updated data
     * @throws IllegalArgumentException if user is null or has null ID
     * @throws UserNotFoundException if user with specified ID doesn't exist
     * @throws LoginAlreadyExistsException if login change conflicts with existing user
     * @throws ValidationException if user data fails business validation
     * @throws DataAccessException if persistence operation fails
     * @see User
     */
    User updateUser(User user);
    
    /**
     * Deletes a user from the system.
     * 
     * <p>This method handles the permanent removal of a user from the system.
     * The operation includes existence verification and handles any cascading
     * effects or cleanup operations that may be required.
     * 
     * <p><strong>Business Rules:</strong>
     * <ul>
     *   <li>User ID must not be null and must exist in the system</li>
     *   <li>Deletion is permanent and cannot be undone</li>
     *   <li>May trigger cascading operations on related data</li>
     *   <li>Should maintain data integrity constraints</li>
     * </ul>
     * 
     * <p><strong>Process Flow:</strong>
     * <ol>
     *   <li>Validate input ID</li>
     *   <li>Verify user existence</li>
     *   <li>Handle cascading deletions if applicable</li>
     *   <li>Remove user from repository</li>
     *   <li>Clean up any related resources</li>
     * </ol>
     * 
     * @param id the unique identifier of the user to delete
     * @throws IllegalArgumentException if ID is null or invalid format
     * @throws UserNotFoundException if user with specified ID doesn't exist
     * @throws DataAccessException if deletion operation fails
     * @throws BusinessRuleException if deletion violates business constraints
     * @see User
     */
    void deleteUser(String id);
}
