package jalau.usersapi.config;

import jalau.usersapi.core.application.UserCommandService;
import jalau.usersapi.core.domain.repositories.IUserRepository;
import jalau.usersapi.core.domain.services.IUserCommandService;
import jalau.usersapi.infrastructure.mysql.mappers.DbUserToUserMapper;
import jalau.usersapi.infrastructure.mysql.mappers.UserToDbUserMapper;
import jalau.usersapi.presentation.mappers.UserCreateDtoToUserMapper;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import jalau.usersapi.presentation.validators.UserCreateDtoValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for dependency injection
 * 
 * This configuration class defines beans for the application
 * following Clean Architecture principles.
 */
@Configuration
public class ApplicationConfig {
    
    /**
     * Bean for UserCommandService
     */
    @Bean
    public IUserCommandService userCommandService(IUserRepository userRepository) {
        return new UserCommandService(userRepository);
    }
    
    /**
     * Bean for UserCreateDtoToUserMapper
     */
    @Bean
    public UserCreateDtoToUserMapper userCreateDtoToUserMapper() {
        return new UserCreateDtoToUserMapper();
    }
    
    /**
     * Bean for UserToUserResponseDtoMapper
     */
    @Bean
    public UserToUserResponseDtoMapper userToUserResponseDtoMapper() {
        return new UserToUserResponseDtoMapper();
    }
    
    /**
     * Bean for UserCreateDtoValidator
     */
    @Bean
    public UserCreateDtoValidator userCreateDtoValidator() {
        return new UserCreateDtoValidator();
    }
    
    /**
     * Bean for UserToDbUserMapper
     */
    @Bean
    public UserToDbUserMapper userToDbUserMapper() {
        return new UserToDbUserMapper();
    }
    
    /**
     * Bean for DbUserToUserMapper
     */
    @Bean
    public DbUserToUserMapper dbUserToUserMapper() {
        return new DbUserToUserMapper();
    }
}