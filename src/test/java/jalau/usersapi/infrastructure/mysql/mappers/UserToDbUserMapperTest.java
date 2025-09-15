package jalau.usersapi.infrastructure.mysql.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.infrastructure.mysql.entities.DbUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserToDbUserMapper
 * 
 * This test class validates the mapping logic from User domain entity
 * to DbUser database entity.
 */
class UserToDbUserMapperTest {
    
    private UserToDbUserMapper mapper;
    
    @BeforeEach
    void setUp() {
        mapper = new UserToDbUserMapper();
    }
    
    @Test
    void map_ValidUser_ReturnsDbUser() {
        // Arrange
        User user = new User("123e4567-e89b-12d3-a456-426614174000", "John Doe", "johndoe", "password123");
        
        // Act
        DbUser dbUser = mapper.map(user);
        
        // Assert
        assertNotNull(dbUser);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", dbUser.getId());
        assertEquals("John Doe", dbUser.getName());
        assertEquals("johndoe", dbUser.getLogin());
        assertEquals("password123", dbUser.getPassword());
    }
    
    @Test
    void map_NullUser_ReturnsNull() {
        // Act
        DbUser dbUser = mapper.map(null);
        
        // Assert
        assertNull(dbUser);
    }
    
    @Test
    void map_UserWithNullFields_MapsCorrectly() {
        // Arrange
        User user = new User(null, null, null, null);
        
        // Act
        DbUser dbUser = mapper.map(user);
        
        // Assert
        assertNotNull(dbUser);
        assertNull(dbUser.getId());
        assertNull(dbUser.getName());
        assertNull(dbUser.getLogin());
        assertNull(dbUser.getPassword());
    }
    
    @Test
    void map_UserWithEmptyFields_MapsCorrectly() {
        // Arrange
        User user = new User("", "", "", "");
        
        // Act
        DbUser dbUser = mapper.map(user);
        
        // Assert
        assertNotNull(dbUser);
        assertEquals("", dbUser.getId());
        assertEquals("", dbUser.getName());
        assertEquals("", dbUser.getLogin());
        assertEquals("", dbUser.getPassword());
    }
}