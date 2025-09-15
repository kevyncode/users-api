package jalau.usersapi.presentation.validators;

import jalau.usersapi.presentation.dtos.UserCreateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserCreateDtoValidator
 * 
 * This test class validates the UserCreateDto validation logic
 * ensuring all required fields are properly validated.
 */
class UserCreateDtoValidatorTest {
    
    private UserCreateDtoValidator validator;
    
    @BeforeEach
    void setUp() {
        validator = new UserCreateDtoValidator();
    }
    
    @Test
    void validate_ValidDto_ReturnsEmptyList() {
        // Arrange
        UserCreateDto validDto = new UserCreateDto("John Doe", "johndoe", "password123");
        
        // Act
        List<String> errors = validator.validate(validDto);
        
        // Assert
        assertTrue(errors.isEmpty());
    }
    
    @Test
    void validate_NullDto_ReturnsError() {
        // Act
        List<String> errors = validator.validate(null);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals("Request body is required", errors.get(0));
    }
    
    @Test
    void validate_NullName_ReturnsError() {
        // Arrange
        UserCreateDto dto = new UserCreateDto(null, "johndoe", "password123");
        
        // Act
        List<String> errors = validator.validate(dto);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals("name is required", errors.get(0));
    }
    
    @Test
    void validate_EmptyName_ReturnsError() {
        // Arrange
        UserCreateDto dto = new UserCreateDto("", "johndoe", "password123");
        
        // Act
        List<String> errors = validator.validate(dto);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals("name cannot be empty", errors.get(0));
    }
    
    @Test
    void validate_NullLogin_ReturnsError() {
        // Arrange
        UserCreateDto dto = new UserCreateDto("John Doe", null, "password123");
        
        // Act
        List<String> errors = validator.validate(dto);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals("login is required", errors.get(0));
    }
    
    @Test
    void validate_EmptyLogin_ReturnsError() {
        // Arrange
        UserCreateDto dto = new UserCreateDto("John Doe", " ", "password123");
        
        // Act
        List<String> errors = validator.validate(dto);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals("login cannot be empty", errors.get(0));
    }
    
    @Test
    void validate_NullPassword_ReturnsError() {
        // Arrange
        UserCreateDto dto = new UserCreateDto("John Doe", "johndoe", null);
        
        // Act
        List<String> errors = validator.validate(dto);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals("password is required", errors.get(0));
    }
    
    @Test
    void validate_EmptyPassword_ReturnsError() {
        // Arrange
        UserCreateDto dto = new UserCreateDto("John Doe", "johndoe", "");
        
        // Act
        List<String> errors = validator.validate(dto);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals("password cannot be empty", errors.get(0));
    }
    
    @Test
    void validate_MultipleErrors_ReturnsAllErrors() {
        // Arrange
        UserCreateDto dto = new UserCreateDto(null, null, null);
        
        // Act
        List<String> errors = validator.validate(dto);
        
        // Assert
        assertEquals(3, errors.size());
        assertTrue(errors.contains("name is required"));
        assertTrue(errors.contains("login is required"));
        assertTrue(errors.contains("password is required"));
    }
    
    @Test
    void isValid_ValidDto_ReturnsTrue() {
        // Arrange
        UserCreateDto validDto = new UserCreateDto("John Doe", "johndoe", "password123");
        
        // Act & Assert
        assertTrue(validator.isValid(validDto));
    }
    
    @Test
    void isValid_InvalidDto_ReturnsFalse() {
        // Arrange
        UserCreateDto invalidDto = new UserCreateDto(null, "johndoe", "password123");
        
        // Act & Assert
        assertFalse(validator.isValid(invalidDto));
    }
}