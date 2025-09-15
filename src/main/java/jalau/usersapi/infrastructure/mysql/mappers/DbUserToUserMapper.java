package jalau.usersapi.infrastructure.mysql.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.infrastructure.mysql.entities.DbUser;

/**
 * Mapper to convert DbUser database entity to User domain entity
 * 
 * This mapper handles the conversion from infrastructure layer database entities
 * to domain layer entities.
 */
public class DbUserToUserMapper {
    
    /**
     * Maps a DbUser database entity to a User domain entity
     * 
     * @param dbUser the database entity to convert
     * @return User domain entity
     */
    public User map(DbUser dbUser) {
        if (dbUser == null) {
            return null;
        }
        
        User user = new User();
        user.setId(dbUser.getId());
        user.setName(dbUser.getName());
        user.setLogin(dbUser.getLogin());
        user.setPassword(dbUser.getPassword());
        
        return user;
    }
}