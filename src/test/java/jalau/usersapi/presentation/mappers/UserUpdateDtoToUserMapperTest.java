package jalau.usersapi.presentation.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.presentation.dtos.UserUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserUpdateDtoToUserMapperTest {

    private UserUpdateDtoToUserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserUpdateDtoToUserMapper();
    }

    @Test
    void map_ValidDto_ReturnsUser() {
        UserUpdateDto dto = new UserUpdateDto("Luan Gameplays", "luanRato", "password123");
        User user = mapper.map(dto);

        assertNotNull(user);
        assertEquals("Luan Gameplays", user.getName());
        assertEquals("luanRato", user.getLogin());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void map_PartialDto_ReturnsUserWithProvidedFields() {
        UserUpdateDto dto = new UserUpdateDto("Luan Gameplays", null, null);
        User user = mapper.map(dto);

        assertNotNull(user);
        assertEquals("Luan Gameplays", user.getName());
        assertNull(user.getLogin());
        assertNull(user.getPassword());
    }
}