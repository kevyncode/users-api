package jalau.usersapi.infrastructure.mysql.entities;

import jakarta.persistence.*;

/**
 * Database entity for User table
 * 
 * This entity represents the user table structure in the MySQL database.
 * It's only used within the infrastructure layer and unknown outside of infrastructure/mysql.
 */
@Entity
@Table(name = "users")
public class DbUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "login", nullable = false, unique = true)
    private String login;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    /**
     * Default constructor
     */
    public DbUser() {
    }
    
    /**
     * Constructor with all fields
     * 
     * @param id the user's unique identifier
     * @param name the user's display name
     * @param login the user's login username
     * @param password the user's password
     */
    public DbUser(String id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }
    
    /**
     * Gets the user's unique identifier
     * 
     * @return the user's id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Sets the user's unique identifier
     * 
     * @param id the user's id
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Gets the user's display name
     * 
     * @return the user's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the user's display name
     * 
     * @param name the user's name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the user's login username
     * 
     * @return the user's login
     */
    public String getLogin() {
        return login;
    }
    
    /**
     * Sets the user's login username
     * 
     * @param login the user's login
     */
    public void setLogin(String login) {
        this.login = login;
    }
    
    /**
     * Gets the user's password
     * 
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the user's password
     * 
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}