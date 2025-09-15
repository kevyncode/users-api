package jalau.usersapi.presentation.validators;

import jalau.usersapi.presentation.dtos.UserCreateDto;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator for UserCreateDto
 * 
 * This validator ensures that UserCreateDto fields meet the required criteria
 * for creating a new user in the system.
 */
public class UserCreateDtoValidator {
    
    /**
     * Validates a UserCreateDto instance
     * 
     * @param userCreateDto the DTO to validate
     * @return List of validation error messages, empty if valid
     */
    public List<String> validate(UserCreateDto userCreateDto) {
        List<String> errors = new ArrayList<>();
        
        if (userCreateDto == null) {
            errors.add("Request body is required");
            return errors;
        }
        
        // Validate name
        if (userCreateDto.getName() == null) {
            errors.add("name is required");
        } else if (userCreateDto.getName().trim().isEmpty()) {
            errors.add("name cannot be empty");
        }
        
        // Validate login
        if (userCreateDto.getLogin() == null) {
            errors.add("login is required");
        } else if (userCreateDto.getLogin().trim().isEmpty()) {
            errors.add("login cannot be empty");
        }
        
        // Validate password
        if (userCreateDto.getPassword() == null) {
            errors.add("password is required");
        } else if (userCreateDto.getPassword().trim().isEmpty()) {
            errors.add("password cannot be empty");
        }
        
        return errors;
    }
    
    /**
     * Checks if a UserCreateDto is valid
     * 
     * @param userCreateDto the DTO to validate
     * @return true if valid, false otherwise
     */
    public boolean isValid(UserCreateDto userCreateDto) {
        return validate(userCreateDto).isEmpty();
    }
}