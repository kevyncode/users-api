package jalau.usersapi.core.application;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.services.IUserQueryService;

import java.util.List;

/**
 * Service implementation for user query operations
 * 
 * This service will implement the business logic for user query operations
 * following the Clean Architecture pattern.
 */
public class UserQueryService implements IUserQueryService {
    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public User getUser(String id) {
        return null;
    }

    // TODO: Implement user query service methods in upcoming user stories
    // This will include getAllUsers() and getUserById() methods
}
