package jalau.usersapi.presentation.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.presentation.dtos.UserCreateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserCreateDtoToUserMapper
 * 
 * This test class validates the mapping logic from UserCreateDto
 * to User domain entity.
 */
class UserCreateDtoToUserMapperTest {
    
    private UserCreateDtoToUserMapper mapper;
    
    @BeforeEach
    void setUp() {
        mapper = new UserCreateDtoToUserMapper();
    }
    
    @Test
    void map_ValidDto_ReturnsUserWithNullId() {
        // Arrange
        UserCreateDto dto = new UserCreateDto("John Doe", "johndoe", "password123");
        
        // Act
        User user = mapper.map(dto);
        
        // Assert
        assertNotNull(user);
        assertNull(user.getId()); // ID should be null for creation
        assertEquals("John Doe", user.getName());
        assertEquals("johndoe", user.getLogin());
        assertEquals("password123", user.getPassword());
    }
    
    @Test
    void map_NullDto_ReturnsNull() {
        // Act
        User user = mapper.map(null);
        
        // Assert
        assertNull(user);
    }
    
    @Test
    void map_DtoWithNullFields_MapsCorrectly() {
        // Arrange
        UserCreateDto dto = new UserCreateDto(null, null, null);
        
        // Act
        User user = mapper.map(dto);
        
        // Assert
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getLogin());
        assertNull(user.getPassword());
    }
    
    @Test
    void map_DtoWithEmptyFields_MapsCorrectly() {
        // Arrange
        UserCreateDto dto = new UserCreateDto("", "", "");
        
        // Act
        User user = mapper.map(dto);
        
        // Assert
        assertNotNull(user);
        assertNull(user.getId());
        assertEquals("", user.getName());
        assertEquals("", user.getLogin());
        assertEquals("", user.getPassword());
    }
}