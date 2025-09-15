package jalau.usersapi.infrastructure.mysql.repositories;

import jalau.usersapi.infrastructure.mysql.entities.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository interface for DbUser database operations
 * 
 * This interface extends JpaRepository to provide CRUD operations
 * for the DbUser entity with Spring Data JPA.
 */
@Repository
public interface DbUserJpaRepository extends JpaRepository<DbUser, String> {
    
    /**
     * Finds a user by their login
     * 
     * @param login the login to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<DbUser> findByLogin(String login);
}