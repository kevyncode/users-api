package jalau.usersapi.presentation.validators;

import jalau.usersapi.presentation.dtos.UserUpdateDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator for UserUpdateDto
 */
@Component
public class UserUpdateDtoValidator {

    public List<String> validate(UserUpdateDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Request body is required");
            return errors;
        }

        // Validate name (optional but if provided, not empty)
        if (dto.getName() != null && dto.getName().trim().isEmpty()) {
            errors.add("name cannot be empty");
        }

        // Validate login (optional but if provided, not empty)
        if (dto.getLogin() != null && dto.getLogin().trim().isEmpty()) {
            errors.add("login cannot be empty");
        }

        // Validate password (optional but if provided, not empty)
        if (dto.getPassword() != null && dto.getPassword().trim().isEmpty()) {
            errors.add("password cannot be empty");
        }

        return errors;
    }

    public boolean isValid(UserUpdateDto dto) {
        return validate(dto).isEmpty();
    }
}