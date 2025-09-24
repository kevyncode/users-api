package jalau.usersapi.core.domain.entities;

/**
 * User domain entity representing a user in the system.
 * 
 * <p>This class represents the core User entity in the domain layer, following
 * Clean Architecture principles. It contains the essential attributes and business
 * logic for user management operations.
 * 
 * <p>The User entity is used across all layers of the application:
 * <ul>
 *   <li><strong>Domain Layer:</strong> Core business entity</li>
 *   <li><strong>Application Layer:</strong> Used in services for business operations</li>
 *   <li><strong>Infrastructure Layer:</strong> Mapped to/from database entities</li>
 *   <li><strong>Presentation Layer:</strong> Mapped to/from DTOs</li>
 * </ul>
 * 
 * <p><strong>Business Rules:</strong>
 * <ul>
 *   <li>ID must be a unique UUID string</li>
 *   <li>Name and login are required fields</li>
 *   <li>Login must be unique across the system</li>
 *   <li>Password is required for user creation</li>
 * </ul>
 * 
 * @author Users API Development Team
 * @since 1.0.0
 */
public class User {
    private String id;
    private String name;
    private String login;
    private String password;

    /**
     * Default constructor for framework compatibility.
     */
    public User() {}

    /**
     * Constructs a User with all required fields.
     * 
     * @param id unique identifier for the user (UUID format expected)
     * @param name display name of the user
     * @param login unique login identifier for the user
     * @param password user's password (should be encrypted in production)
     */
    public User(String id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    /**
     * Gets the unique identifier of the user.
     * 
     * @return the user's unique ID (UUID format)
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     * 
     * @param id the unique ID to set (UUID format expected)
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the display name of the user.
     * 
     * @return the user's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name of the user.
     * 
     * @param name the display name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique login identifier of the user.
     * 
     * @return the user's login identifier
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the unique login identifier of the user.
     * 
     * @param login the login identifier to set (must be unique)
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets the user's password.
     * 
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * 
     * @param password the password to set (should be encrypted)
     */
    public void setPassword(String password) {
        this.password = password;
    }
}