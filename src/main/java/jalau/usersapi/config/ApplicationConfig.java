package jalau.usersapi.config;

import jalau.usersapi.core.application.UserCommandService;
import jalau.usersapi.core.application.UserQueryService;
import jalau.usersapi.core.domain.repositories.IUserRepository;
import jalau.usersapi.core.domain.services.IUserCommandService;
import jalau.usersapi.core.domain.services.IUserQueryService;
import jalau.usersapi.infrastructure.mysql.mappers.DbUserToUserMapper;
import jalau.usersapi.infrastructure.mysql.mappers.UserToDbUserMapper;
import jalau.usersapi.presentation.mappers.UserCreateDtoToUserMapper;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import jalau.usersapi.presentation.mappers.UserUpdateDtoToUserMapper;
import jalau.usersapi.presentation.validators.UserCreateDtoValidator;
import jalau.usersapi.presentation.validators.UserUpdateDtoValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for dependency injection following Clean Architecture principles.
 * 
 * <p>This configuration class defines all application beans organized by layers:
 * <ul>
 *   <li><strong>Services:</strong> Business logic implementations for CQRS pattern</li>
 *   <li><strong>Mappers:</strong> Object transformation between layers</li>
 *   <li><strong>Validators:</strong> Input validation for presentation layer</li>
 * </ul>
 * 
 * <p>The configuration ensures proper dependency injection and maintains the
 * independence of each architectural layer by providing concrete implementations
 * of interfaces defined in the domain layer.
 * 
 * @author Users API Development Team
 * @since 1.0.0
 */
@Configuration
public class ApplicationConfig {
    
    /**
     * Creates the command service implementation for user write operations.
     * 
     * <p>This service handles all user creation, modification, and deletion operations
     * following the Command Query Responsibility Segregation (CQRS) pattern.
     * 
     * @param userRepository the repository implementation for data persistence
     * @return configured user command service
     */
    @Bean
    public IUserCommandService userCommandService(IUserRepository userRepository) {
        return new UserCommandService(userRepository);
    }

    /**
     * Creates the query service implementation for user read operations.
     * 
     * <p>This service handles all user retrieval operations following the
     * Command Query Responsibility Segregation (CQRS) pattern.
     * 
     * @param userRepository the repository implementation for data access
     * @return configured user query service
     */
    @Bean
    public IUserQueryService userQueryService(IUserRepository userRepository) {
        return new UserQueryService(userRepository);
    }

    /**
     * Creates mapper for transforming user creation DTOs to domain entities.
     * 
     * @return user creation DTO mapper
     */
    @Bean
    public UserCreateDtoToUserMapper userCreateDtoToUserMapper() {
        return new UserCreateDtoToUserMapper();
    }

    /**
     * Creates mapper for transforming user update DTOs to domain entities.
     * 
     * @return user update DTO mapper
     */
    @Bean
    public UserUpdateDtoToUserMapper userUpdateDtoToUserMapper() {
        return new UserUpdateDtoToUserMapper();
    }

    /**
     * Creates mapper for transforming domain entities to response DTOs.
     * 
     * @return user to response DTO mapper
     */
    @Bean
    public UserToUserResponseDtoMapper userToUserResponseDtoMapper() {
        return new UserToUserResponseDtoMapper();
    }

    /**
     * Creates mapper for transforming domain entities to database entities.
     * 
     * @return domain to database entity mapper
     */
    @Bean
    public UserToDbUserMapper userToDbUserMapper() {
        return new UserToDbUserMapper();
    }

    /**
     * Creates mapper for transforming database entities to domain entities.
     * 
     * @return database to domain entity mapper
     */
    @Bean
    public DbUserToUserMapper dbUserToUserMapper() {
        return new DbUserToUserMapper();
    }

    /**
     * Creates validator for user creation DTOs.
     * 
     * <p>Validates incoming data for user creation requests ensuring
     * business rules compliance before processing.
     * 
     * @return user creation DTO validator
     */
    @Bean
    public UserCreateDtoValidator userCreateDtoValidator() {
        return new UserCreateDtoValidator();
    }

    /**
     * Creates validator for user update DTOs.
     * 
     * <p>Validates incoming data for user modification requests ensuring
     * business rules compliance before processing.
     * 
     * @return user update DTO validator
     */
    @Bean
    public UserUpdateDtoValidator userUpdateDtoValidator() {
        return new UserUpdateDtoValidator();
    }
}