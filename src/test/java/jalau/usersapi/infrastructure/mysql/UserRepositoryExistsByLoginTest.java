package jalau.usersapi.infrastructure.mysql;

import jalau.usersapi.infrastructure.mysql.entities.DbUser;
import jalau.usersapi.infrastructure.mysql.mappers.DbUserToUserMapper;
import jalau.usersapi.infrastructure.mysql.mappers.UserToDbUserMapper;
import jalau.usersapi.infrastructure.mysql.repositories.DbUserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserRepository existsByLogin method
 */
@ExtendWith(MockitoExtension.class)
public class UserRepositoryExistsByLoginTest {

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
    void existsByLogin_WhenUserExists_ShouldReturnTrue() {
        // Arrange
        String login = "joao.silva";
        DbUser dbUser = new DbUser();
        dbUser.setId("123e4567-e89b-12d3-a456-426614174000");
        dbUser.setLogin(login);
        
        when(dbUserJpaRepository.findByLogin(login)).thenReturn(Optional.of(dbUser));

        // Act
        boolean result = userRepository.existsByLogin(login);

        // Assert
        assertTrue(result);
        verify(dbUserJpaRepository, times(1)).findByLogin(login);
    }

    @Test
    void existsByLogin_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Arrange
        String login = "nonexistent.user";
        
        when(dbUserJpaRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Act
        boolean result = userRepository.existsByLogin(login);

        // Assert
        assertFalse(result);
        verify(dbUserJpaRepository, times(1)).findByLogin(login);
    }

    @Test
    void existsByLogin_WhenLoginIsNull_ShouldReturnFalse() {
        // Arrange
        String login = null;
        
        when(dbUserJpaRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Act
        boolean result = userRepository.existsByLogin(login);

        // Assert
        assertFalse(result);
        verify(dbUserJpaRepository, times(1)).findByLogin(login);
    }

    @Test
    void existsByLogin_WhenLoginIsEmpty_ShouldReturnFalse() {
        // Arrange
        String login = "";
        
        when(dbUserJpaRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Act
        boolean result = userRepository.existsByLogin(login);

        // Assert
        assertFalse(result);
        verify(dbUserJpaRepository, times(1)).findByLogin(login);
    }
}