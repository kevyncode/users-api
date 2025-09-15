package jalau.usersapi.presentation.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.presentation.dtos.UserResponseDto;

/**
 * Mapper to convert User entity to UserResponseDto
 * 
 * This mapper handles the conversion from domain layer entities
 * to presentation layer DTOs following Clean Architecture principles.
 */
public class UserToUserResponseDtoMapper {
    
    /**
     * Maps a User entity to a UserResponseDto
     * 
     * @param user the User entity to convert
     * @return UserResponseDto without password for security
     */
    public UserResponseDto map(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setLogin(user.getLogin());
        // Password is intentionally excluded for security reasons
        
        return userResponseDto;
    }
}