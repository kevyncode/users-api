package jalau.usersapi.presentation.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.presentation.dtos.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserToUserResponseDtoMapper
 * 
 * This test class validates the mapping logic from User domain entity
 * to UserResponseDto, ensuring password is excluded for security.
 */
class UserToUserResponseDtoMapperTest {
    
    private UserToUserResponseDtoMapper mapper;
    
    @BeforeEach
    void setUp() {
        mapper = new UserToUserResponseDtoMapper();
    }
    
    @Test
    void map_ValidUser_ReturnsResponseDtoWithoutPassword() {
        // Arrange
        User user = new User("123e4567-e89b-12d3-a456-426614174000", "John Doe", "johndoe", "password123");
        
        // Act
        UserResponseDto responseDto = mapper.map(user);
        
        // Assert
        assertNotNull(responseDto);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", responseDto.getId());
        assertEquals("John Doe", responseDto.getName());
        assertEquals("johndoe", responseDto.getLogin());
        // Password should not be in the response
    }
    
    @Test
    void map_NullUser_ReturnsNull() {
        // Act
        UserResponseDto responseDto = mapper.map(null);
        
        // Assert
        assertNull(responseDto);
    }
    
    @Test
    void map_UserWithNullFields_MapsCorrectly() {
        // Arrange
        User user = new User(null, null, null, "password123");
        
        // Act
        UserResponseDto responseDto = mapper.map(user);
        
        // Assert
        assertNotNull(responseDto);
        assertNull(responseDto.getId());
        assertNull(responseDto.getName());
        assertNull(responseDto.getLogin());
    }
    
    @Test
    void map_UserWithEmptyFields_MapsCorrectly() {
        // Arrange
        User user = new User("", "", "", "password123");
        
        // Act
        UserResponseDto responseDto = mapper.map(user);
        
        // Assert
        assertNotNull(responseDto);
        assertEquals("", responseDto.getId());
        assertEquals("", responseDto.getName());
        assertEquals("", responseDto.getLogin());
    }
}