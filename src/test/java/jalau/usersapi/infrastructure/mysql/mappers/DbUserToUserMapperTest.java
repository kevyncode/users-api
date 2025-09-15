package jalau.usersapi.infrastructure.mysql.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.infrastructure.mysql.entities.DbUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DbUserToUserMapper
 * 
 * This test class validates the mapping logic from DbUser database entity
 * to User domain entity.
 */
class DbUserToUserMapperTest {
    
    private DbUserToUserMapper mapper;
    
    @BeforeEach
    void setUp() {
        mapper = new DbUserToUserMapper();
    }
    
    @Test
    void map_ValidDbUser_ReturnsUser() {
        // Arrange
        DbUser dbUser = new DbUser("123e4567-e89b-12d3-a456-426614174000", "John Doe", "johndoe", "password123");
        
        // Act
        User user = mapper.map(dbUser);
        
        // Assert
        assertNotNull(user);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("johndoe", user.getLogin());
        assertEquals("password123", user.getPassword());
    }
    
    @Test
    void map_NullDbUser_ReturnsNull() {
        // Act
        User user = mapper.map(null);
        
        // Assert
        assertNull(user);
    }
    
    @Test
    void map_DbUserWithNullFields_MapsCorrectly() {
        // Arrange
        DbUser dbUser = new DbUser(null, null, null, null);
        
        // Act
        User user = mapper.map(dbUser);
        
        // Assert
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getLogin());
        assertNull(user.getPassword());
    }
    
    @Test
    void map_DbUserWithEmptyFields_MapsCorrectly() {
        // Arrange
        DbUser dbUser = new DbUser("", "", "", "");
        
        // Act
        User user = mapper.map(dbUser);
        
        // Assert
        assertNotNull(user);
        assertEquals("", user.getId());
        assertEquals("", user.getName());
        assertEquals("", user.getLogin());
        assertEquals("", user.getPassword());
    }
}