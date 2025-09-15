package jalau.usersapi.core.application;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.exceptions.LoginAlreadyExistsException;
import jalau.usersapi.core.domain.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserCommandService with login duplication validation
 */
@ExtendWith(MockitoExtension.class)
public class UserCommandServiceLoginValidationTest {

    @Mock
    private IUserRepository userRepository;

    private UserCommandService userCommandService;

    @BeforeEach
    void setUp() {
        userCommandService = new UserCommandService(userRepository);
    }

    @Test
    void createUser_WhenLoginDoesNotExist_ShouldCreateUser() {
        // Arrange
        User userToCreate = new User(null, "João Silva", "joao.silva", "senha123");
        User createdUser = new User("123e4567-e89b-12d3-a456-426614174000", "João Silva", "joao.silva", "senha123");
        
        when(userRepository.existsByLogin("joao.silva")).thenReturn(false);
        when(userRepository.createUser(userToCreate)).thenReturn(createdUser);

        // Act
        User result = userCommandService.createUser(userToCreate);

        // Assert
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
        assertEquals("João Silva", result.getName());
        assertEquals("joao.silva", result.getLogin());
        
        verify(userRepository, times(1)).existsByLogin("joao.silva");
        verify(userRepository, times(1)).createUser(userToCreate);
    }

    @Test
    void createUser_WhenLoginAlreadyExists_ShouldThrowLoginAlreadyExistsException() {
        // Arrange
        User userToCreate = new User(null, "João Silva", "joao.silva", "senha123");
        
        when(userRepository.existsByLogin("joao.silva")).thenReturn(true);

        // Act & Assert
        LoginAlreadyExistsException exception = assertThrows(
            LoginAlreadyExistsException.class,
            () -> userCommandService.createUser(userToCreate)
        );
        
        assertEquals("User with login 'joao.silva' already exists", exception.getMessage());
        
        verify(userRepository, times(1)).existsByLogin("joao.silva");
        verify(userRepository, never()).createUser(any());
    }

    @Test
    void createUser_WhenLoginExistsWithDifferentCase_ShouldThrowLoginAlreadyExistsException() {
        // Arrange
        User userToCreate = new User(null, "João Silva", "JOAO.SILVA", "senha123");
        
        when(userRepository.existsByLogin("JOAO.SILVA")).thenReturn(true);

        // Act & Assert
        LoginAlreadyExistsException exception = assertThrows(
            LoginAlreadyExistsException.class,
            () -> userCommandService.createUser(userToCreate)
        );
        
        assertEquals("User with login 'JOAO.SILVA' already exists", exception.getMessage());
        
        verify(userRepository, times(1)).existsByLogin("JOAO.SILVA");
        verify(userRepository, never()).createUser(any());
    }
}