package jalau.usersapi.presentation.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.presentation.dtos.UserUpdateDto;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert UserUpdateDto to User domain entity
 */
@Component
public class UserUpdateDtoToUserMapper {

    public User map(UserUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());

        return user;
    }
}