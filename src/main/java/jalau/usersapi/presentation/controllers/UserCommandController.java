package jalau.usersapi.presentation.controllers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.exceptions.LoginAlreadyExistsException;
import jalau.usersapi.core.domain.services.IUserCommandService;
import jalau.usersapi.presentation.dtos.UserCreateDto;
import jalau.usersapi.presentation.dtos.UserResponseDto;
import jalau.usersapi.presentation.mappers.UserCreateDtoToUserMapper;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import jalau.usersapi.presentation.validators.UserCreateDtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for User command operations
 * 
 * This controller handles all HTTP POST, PATCH, and DELETE requests related to users.
 * It follows the Command Query Responsibility Segregation (CQRS) pattern.
 */
@RestController
@RequestMapping("/api/users")
public class UserCommandController {
    
    private final IUserCommandService userCommandService;
    private final UserCreateDtoToUserMapper userCreateDtoToUserMapper;
    private final UserToUserResponseDtoMapper userToUserResponseDtoMapper;
    private final UserCreateDtoValidator userCreateDtoValidator;
    
    /**
     * Constructor for dependency injection
     * 
     * @param userCommandService service for user command operations
     * @param userCreateDtoToUserMapper mapper from DTO to User entity
     * @param userToUserResponseDtoMapper mapper from User entity to response DTO
     * @param userCreateDtoValidator validator for user creation requests
     */
    public UserCommandController(IUserCommandService userCommandService,
                                UserCreateDtoToUserMapper userCreateDtoToUserMapper,
                                UserToUserResponseDtoMapper userToUserResponseDtoMapper,
                                UserCreateDtoValidator userCreateDtoValidator) {
        this.userCommandService = userCommandService;
        this.userCreateDtoToUserMapper = userCreateDtoToUserMapper;
        this.userToUserResponseDtoMapper = userToUserResponseDtoMapper;
        this.userCreateDtoValidator = userCreateDtoValidator;
    }
    
    /**
     * Creates a new user
     * 
     * @param userCreateDto the user creation request body
     * @return ResponseEntity with created user data, validation errors, or conflict
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userCreateDto) {
        // Validate request body
        List<String> validationErrors = userCreateDtoValidator.validate(userCreateDto);
        if (!validationErrors.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", validationErrors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        try {
            // Map DTO to domain entity
            User userToCreate = userCreateDtoToUserMapper.map(userCreateDto);
            
            // Call service to create user
            User createdUser = userCommandService.createUser(userToCreate);
            
            // Map domain entity to response DTO
            UserResponseDto userResponseDto = userToUserResponseDtoMapper.map(createdUser);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
            
        } catch (LoginAlreadyExistsException e) {
            // Return 409 Conflict when login already exists
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }
}
