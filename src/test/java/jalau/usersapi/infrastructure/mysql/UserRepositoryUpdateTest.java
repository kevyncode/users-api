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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryUpdateTest {

    @Mock
    private DbUserJpaRepository dbUserJpaRepository;

    @Mock
    private UserToDbUserMapper userToDbUserMapper;

    @Mock
    private DbUserToUserMapper dbUserToUserMapper;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(dbUserJpaRepository, userToDbUserMapper, dbUserToUserMapper);
    }

    @Test
    void updateUser_UserExists_ReturnsUpdatedUser() {
        User userToUpdate = new User("1", "New Name", "newlogin", "newpassword");
        DbUser dbUser = new DbUser("1", "New Name", "newlogin", "newpassword");
        User expectedUser = new User("1", "New Name", "newlogin", "newpassword");

        when(dbUserJpaRepository.findById("1")).thenReturn(Optional.of(new DbUser()));
        when(userToDbUserMapper.map(userToUpdate)).thenReturn(dbUser);
        when(dbUserJpaRepository.save(dbUser)).thenReturn(dbUser);
        when(dbUserToUserMapper.map(dbUser)).thenReturn(expectedUser);

        User result = userRepository.updateUser(userToUpdate);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(dbUserJpaRepository, times(1)).save(dbUser);
    }

    @Test
    void updateUser_UserNotFound_ReturnsNull() {
        User userToUpdate = new User("156", "New Name", "newlogin", "newpassword");

        when(dbUserJpaRepository.findById("156")).thenReturn(Optional.empty());

        User result = userRepository.updateUser(userToUpdate);

        assertNull(result);
        verify(dbUserJpaRepository, never()).save(any());
    }
}