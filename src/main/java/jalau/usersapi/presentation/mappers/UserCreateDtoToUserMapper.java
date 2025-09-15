package jalau.usersapi.presentation.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.presentation.dtos.UserCreateDto;

/**
 * Mapper to convert UserCreateDto to User entity
 * 
 * This mapper handles the conversion from presentation layer DTOs
 * to domain layer entities following Clean Architecture principles.
 */
public class UserCreateDtoToUserMapper {
    
    /**
     * Maps a UserCreateDto to a User entity
     * 
     * @param userCreateDto the DTO to convert
     * @return User entity with null id (for creation)
     */
    public User map(UserCreateDto userCreateDto) {
        if (userCreateDto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(null); // ID should be null for creation
        user.setName(userCreateDto.getName());
        user.setLogin(userCreateDto.getLogin());
        user.setPassword(userCreateDto.getPassword());
        
        return user;
    }
}