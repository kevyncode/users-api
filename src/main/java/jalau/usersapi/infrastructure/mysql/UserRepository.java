package jalau.usersapi.infrastructure.mysql;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.repositories.IUserRepository;
import jalau.usersapi.infrastructure.mysql.entities.DbUser;
import jalau.usersapi.infrastructure.mysql.mappers.DbUserToUserMapper;
import jalau.usersapi.infrastructure.mysql.mappers.UserToDbUserMapper;
import jalau.usersapi.infrastructure.mysql.repositories.DbUserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MySQL implementation of user repository
 * This repository handles database operations for the User entity
 * using JPA and MySQL as the persistence layer.
 */
@Repository
public class UserRepository implements IUserRepository {

    private final DbUserJpaRepository dbUserJpaRepository;
    private final UserToDbUserMapper userToDbUserMapper;
    private final DbUserToUserMapper dbUserToUserMapper;

    /**
     * Constructor for dependency injection
     *
     * @param dbUserJpaRepository JPA repository for database operations
     * @param userToDbUserMapper mapper from User to DbUser
     * @param dbUserToUserMapper mapper from DbUser to User
     */
    public UserRepository(DbUserJpaRepository dbUserJpaRepository,
                          UserToDbUserMapper userToDbUserMapper,
                          DbUserToUserMapper dbUserToUserMapper) {
        this.dbUserJpaRepository = dbUserJpaRepository;
        this.userToDbUserMapper = userToDbUserMapper;
        this.dbUserToUserMapper = dbUserToUserMapper;
    }

    /**
     * Creates a new user in the persistence layer
     *
     * @param user the user to create (with null id)
     * @return the created user with assigned id
     */
    @Override
    public User createUser(User user) {
        // Map domain entity to database entity
        DbUser dbUser = userToDbUserMapper.map(user);

        // Save to database (JPA will generate the ID)
        DbUser savedDbUser = dbUserJpaRepository.save(dbUser);

        // Map back to domain entity and return
        return dbUserToUserMapper.map(savedDbUser);
    }

    /**
     * Checks if a user with the specified login already exists
     *
     * @param login the login to check
     * @return true if a user with this login exists, false otherwise
     */
    @Override
    public boolean existsByLogin(String login) {
        return dbUserJpaRepository.findByLogin(login).isPresent();
    }

    /**
     * Updates an existing user in the persistence layer
     *
     * @param user the user to update
     * @return the updated user
     */
    @Override
    public User updateUser(User user) {

        // Check if user exists
        Optional<DbUser> existingDbUser = dbUserJpaRepository.findById(user.getId());
        if (existingDbUser.isEmpty()) {
            return null; // User not found
        }

        // Map domain entity to database entity
        DbUser dbUserToUpdate = userToDbUserMapper.map(user);

        // Ensure the ID is preserved
        dbUserToUpdate.setId(user.getId());

        // Save to database
        DbUser updatedDbUser = dbUserJpaRepository.save(dbUserToUpdate);

        // Map back to domain entity and return
        return dbUserToUserMapper.map(updatedDbUser);
    }

    /**
     * Deletes a user from the persistence layer
     *
     * @param id the unique identifier of the user to delete
     */
    @Override
    public void deleteUser(String id) {
        dbUserJpaRepository.deleteById(id);
    }

    /**
     * Retrieves all users from the persistence layer
     *
     * @return List of all users
     */
    @Override
    public List<User> getUsers() {
        List<DbUser> dbUsers = dbUserJpaRepository.findAll();
        return dbUsers.stream()
                .map(dbUserToUserMapper::map)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single user by their unique identifier
     *
     * @param id the user's unique identifier
     * @return the user with the specified id, or null if not found
     */
    @Override
    public User getUser(String id) {
        return dbUserJpaRepository.findById(id)
                .map(dbUserToUserMapper::map)
                .orElse(null);
    }
}