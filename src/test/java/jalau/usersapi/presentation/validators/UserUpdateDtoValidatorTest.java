package jalau.usersapi.presentation.validators;

import jalau.usersapi.presentation.dtos.UserUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserUpdateDtoValidatorTest {

    private UserUpdateDtoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserUpdateDtoValidator();
    }

    @Test
    void validate_ValidDto_ReturnsEmptyList() {
        UserUpdateDto validDto = new UserUpdateDto("Jurandir", "jirando", "password123");
        List<String> errors = validator.validate(validDto);
        assertTrue(errors.isEmpty());
    }

    @Test
    void validate_NullDto_ReturnsError() {
        List<String> errors = validator.validate(null);
        assertEquals(1, errors.size());
        assertEquals("Request body is required", errors.get(0));
    }

    @Test
    void validate_EmptyName_ReturnsError() {
        UserUpdateDto dto = new UserUpdateDto("", "jirando", "password123");
        List<String> errors = validator.validate(dto);
        assertEquals(1, errors.size());
        assertEquals("name cannot be empty", errors.get(0));
    }

    @Test
    void validate_EmptyLogin_ReturnsError() {
        UserUpdateDto dto = new UserUpdateDto("Jurandir", "", "password123");
        List<String> errors = validator.validate(dto);
        assertEquals(1, errors.size());
        assertEquals("login cannot be empty", errors.get(0));
    }

    @Test
    void validate_EmptyPassword_ReturnsError() {
        UserUpdateDto dto = new UserUpdateDto("Jurandir", "jirando", "");
        List<String> errors = validator.validate(dto);
        assertEquals(1, errors.size());
        assertEquals("password cannot be empty", errors.get(0));
    }

    @Test
    void validate_NullValues_ReturnsNoErrors() {
        UserUpdateDto dto = new UserUpdateDto(null, null, null);
        List<String> errors = validator.validate(dto);
        assertTrue(errors.isEmpty());
    }

}