package jalau.usersapi.infrastructure.mysql.mappers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.infrastructure.mysql.entities.DbUser;

/**
 * Mapper to convert User domain entity to DbUser database entity
 * 
 * This mapper handles the conversion from domain layer entities
 * to infrastructure layer database entities.
 */
public class UserToDbUserMapper {
    
    /**
     * Maps a User domain entity to a DbUser database entity
     * 
     * @param user the domain entity to convert
     * @return DbUser database entity
     */
    public DbUser map(User user) {
        if (user == null) {
            return null;
        }
        
        DbUser dbUser = new DbUser();
        dbUser.setId(user.getId());
        dbUser.setName(user.getName());
        dbUser.setLogin(user.getLogin());
        dbUser.setPassword(user.getPassword());
        
        return dbUser;
    }
}