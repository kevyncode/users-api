package jalau.usersapi.infrastructure.mysql;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.infrastructure.mysql.entities.DbUser;
import jalau.usersapi.infrastructure.mysql.mappers.DbUserToUserMapper;
import jalau.usersapi.infrastructure.mysql.mappers.UserToDbUserMapper;
import jalau.usersapi.infrastructure.mysql.repositories.DbUserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryGetUsersTest {

    @Mock
    private DbUserJpaRepository dbUserJpaRepository;

    @Mock
    private UserToDbUserMapper userToDbUserMapper;

    @Mock
    private DbUserToUserMapper dbUserToUserMapper;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(
                dbUserJpaRepository,
                userToDbUserMapper,
                dbUserToUserMapper
        );
    }

    @Test
    void getUsers_WhenUsersExist_ShouldReturnListOfUsers() {
        // Arrange
        DbUser dbUser1 = new DbUser("1", "John Doe", "johndoe", "password123");
        DbUser dbUser2 = new DbUser("2", "Jane Smith", "janesmith", "password456");
        List<DbUser> dbUsers = Arrays.asList(dbUser1, dbUser2);

        User user1 = new User("1", "John Doe", "johndoe", "password123");
        User user2 = new User("2", "Jane Smith", "janesmith", "password456");

        when(dbUserJpaRepository.findAll()).thenReturn(dbUsers);
        when(dbUserToUserMapper.map(dbUser1)).thenReturn(user1);
        when(dbUserToUserMapper.map(dbUser2)).thenReturn(user2);

        // Act
        List<User> result = userRepository.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        verify(dbUserJpaRepository, times(1)).findAll();
        verify(dbUserToUserMapper, times(2)).map(any(DbUser.class));
    }

    @Test
    void getUsers_WhenNoUsers_ShouldReturnEmptyList() {
        // Arrange
        when(dbUserJpaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userRepository.getUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dbUserJpaRepository, times(1)).findAll();
        verify(dbUserToUserMapper, never()).map(any(DbUser.class));
    }

    @Test
    void getUser_WhenUserExists_ShouldReturnUser() {
        // Arrange
        DbUser dbUser = new DbUser("1", "John Doe", "johndoe", "password123");
        User expectedUser = new User("1", "John Doe", "johndoe", "password123");

        when(dbUserJpaRepository.findById("1")).thenReturn(Optional.of(dbUser));
        when(dbUserToUserMapper.map(dbUser)).thenReturn(expectedUser);

        // Act
        User result = userRepository.getUser("1");

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("johndoe", result.getLogin());
        verify(dbUserJpaRepository, times(1)).findById("1");
        verify(dbUserToUserMapper, times(1)).map(dbUser);
    }

    @Test
    void getUser_WhenUserDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(dbUserJpaRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        User result = userRepository.getUser("999");

        // Assert
        assertNull(result);
        verify(dbUserJpaRepository, times(1)).findById("999");
        verify(dbUserToUserMapper, never()).map(any(DbUser.class));
    }
}