package jalau.usersapi.core.application;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserCommandService
 * 
 * This test class validates the business logic for user command operations
 * ensuring proper delegation to repository layer.
 */
@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {
    
    @Mock
    private IUserRepository userRepository;
    
    private UserCommandService userCommandService;
    
    @BeforeEach
    void setUp() {
        userCommandService = new UserCommandService(userRepository);
    }
    
    @Test
    void createUser_ValidUser_CallsRepositoryAndReturnsCreatedUser() {
        // Arrange
        User inputUser = new User(null, "John Doe", "johndoe", "password123");
        User createdUser = new User("123e4567-e89b-12d3-a456-426614174000", "John Doe", "johndoe", "password123");
        
        when(userRepository.createUser(any(User.class))).thenReturn(createdUser);
        
        // Act
        User result = userCommandService.createUser(inputUser);
        
        // Assert
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("johndoe", result.getLogin());
        assertEquals("password123", result.getPassword());
        
        verify(userRepository, times(1)).createUser(inputUser);
    }
    
    @Test
    void createUser_NullUser_PassesToRepository() {
        // Arrange
        when(userRepository.createUser(null)).thenReturn(null);
        
        // Act
        User result = userCommandService.createUser(null);
        
        // Assert
        assertNull(result);
        verify(userRepository, times(1)).createUser(null);
    }
    
    @Test
    void updateUser_NotImplemented_ThrowsException() {
        // Arrange
        User user = new User("123", "John Doe", "johndoe", "password123");
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userCommandService.updateUser(user);
        });
        
        assertEquals("Not implemented yet", exception.getMessage());
    }
    
    @Test
    void deleteUser_NotImplemented_ThrowsException() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userCommandService.deleteUser("123");
        });
        
        assertEquals("Not implemented yet", exception.getMessage());
    }
}