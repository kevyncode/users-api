package jalau.usersapi.core.application;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.exceptions.UserNotFoundException;
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
    void updateUser_ValidUser_Success() {
        // Arrange
        User existingUser = new User("123", "John Doe", "johndoe", "password123");
        User updateUser = new User("123", "John Doe Updated", "johndoe", "password123");
        User expectedResult = new User("123", "John Doe Updated", "johndoe", "password123");

        // Mock the getUser call to return existing user
        when(userRepository.getUser("123")).thenReturn(existingUser);
        // Mock the updateUser call to return the updated user
        when(userRepository.updateUser(any(User.class))).thenReturn(expectedResult);

        // Act
        User result = userCommandService.updateUser(updateUser);

        // Assert
        assertEquals(expectedResult, result);
        verify(userRepository, times(1)).getUser("123");
        verify(userRepository, times(1)).updateUser(any(User.class));
    }

    @Test
    void updateUser_UserExists_ReturnsUpdatedUser() {
        User existingUser = new User("1", "Name", "login", "password");
        User updatedUser = new User("1", "New Name", "newlogin", "newpassword");

        when(userRepository.getUser("1")).thenReturn(existingUser);
        when(userRepository.existsByLogin("newlogin")).thenReturn(false);
        when(userRepository.updateUser(any(User.class))).thenReturn(updatedUser);

        User result = userCommandService.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("newlogin", result.getLogin());
        verify(userRepository, times(1)).updateUser(any(User.class));
    }

    @Test
    void updateUser_UserNotFound_ReturnsNull() {
        User userToUpdate = new User("569", "New Name", "newlogin", "newpassword");

        when(userRepository.getUser("569")).thenReturn(null);

        User result = userCommandService.updateUser(userToUpdate);

        assertNull(result);
        verify(userRepository, never()).updateUser(any(User.class));
    }

    @Test
    void updateUser_PartialUpdate_MergesFieldsCorrectly() {
        User existingUser = new User("1", "Old Name", "oldlogin", "oldpassword");
        User partialUpdate = new User("1", "New Name", null, null);

        when(userRepository.getUser("1")).thenReturn(existingUser);
        when(userRepository.updateUser(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new User(user.getId(), user.getName(), user.getLogin(), user.getPassword());
        });

        User result = userCommandService.updateUser(partialUpdate);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("oldlogin", result.getLogin());
        assertEquals("oldpassword", result.getPassword());
    }

    // NEW DELETE TESTS - replacing the "not implemented" test
    @Test
    void deleteUser_UserExists_DeletesSuccessfully() {
        // Arrange
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        User existingUser = new User(userId, "John Doe", "johndoe", "password123");

        when(userRepository.getUser(userId)).thenReturn(existingUser);

        // Act & Assert - no exception should be thrown
        assertDoesNotThrow(() -> userCommandService.deleteUser(userId));

        // Verify interactions
        verify(userRepository, times(1)).getUser(userId);
        verify(userRepository, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUser_UserNotFound_ThrowsUserNotFoundException() {
        // Arrange
        String userId = "non-existent-id";

        when(userRepository.getUser(userId)).thenReturn(null);

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userCommandService.deleteUser(userId);
        });

        assertEquals("User with ID 'non-existent-id' not found", exception.getMessage());

        // Verify interactions
        verify(userRepository, times(1)).getUser(userId);
        verify(userRepository, never()).deleteUser(userId);
    }

    @Test
    void deleteUser_NullId_ThrowsUserNotFoundException() {
        // Arrange
        when(userRepository.getUser(null)).thenReturn(null);

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userCommandService.deleteUser(null);
        });

        assertEquals("User with ID 'null' not found", exception.getMessage());

        // Verify interactions
        verify(userRepository, times(1)).getUser(null);
        verify(userRepository, never()).deleteUser(null);
    }
}