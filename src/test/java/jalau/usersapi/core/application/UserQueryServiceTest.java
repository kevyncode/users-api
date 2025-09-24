package jalau.usersapi.core.application;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.repositories.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserQueryService userQueryService;

    @Test
    void shouldReturnListOfUsers() {
        // Arrange
        User user1 = new User("1", "Joatan Carlos", "jcarlos", "password123");
        User user2 = new User("2", "Matheus Victor", "mathVic", "password456");
        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userRepository.getUsers()).thenReturn(expectedUsers);

        // Act
        List<User> result = userQueryService.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Joatan Carlos", result.get(0).getName());
        assertEquals("Matheus Victor", result.get(1).getName());
        verify(userRepository, times(1)).getUsers();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        // Arrange
        when(userRepository.getUsers()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userQueryService.getUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).getUsers();
    }

    @Test
    void shouldReturnUserById() {
        // Arrange
        User expectedUser = new User("1", "Matheus Victor", "mathVic", "password123");

        when(userRepository.getUser("1")).thenReturn(expectedUser);

        // Act
        User result = userQueryService.getUser("1");

        // Assert
        assertNotNull(result);
        assertEquals("Matheus Victor", result.getName());
        assertEquals("mathVic", result.getLogin());
        verify(userRepository, times(1)).getUser("1");
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        // Arrange
        when(userRepository.getUser("562")).thenReturn(null);

        // Act
        User result = userQueryService.getUser("562");

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).getUser("562");
    }
}