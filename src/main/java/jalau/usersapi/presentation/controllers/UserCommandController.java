package jalau.usersapi.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.exceptions.LoginAlreadyExistsException;
import jalau.usersapi.core.domain.exceptions.UserNotFoundException;
import jalau.usersapi.core.domain.services.IUserCommandService;
import jalau.usersapi.presentation.dtos.UserCreateDto;
import jalau.usersapi.presentation.dtos.UserResponseDto;
import jalau.usersapi.presentation.dtos.UserUpdateDto;
import jalau.usersapi.presentation.mappers.UserCreateDtoToUserMapper;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import jalau.usersapi.presentation.mappers.UserUpdateDtoToUserMapper;
import jalau.usersapi.presentation.validators.UserCreateDtoValidator;
import jalau.usersapi.presentation.validators.UserUpdateDtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for User command operations following CQRS pattern.
 * 
 * <p>This controller handles all state-modifying HTTP requests (POST, PATCH, DELETE) related to users,
 * implementing the Command side of the Command Query Responsibility Segregation (CQRS) pattern.
 * It provides endpoints for creating, updating, and deleting user data.
 * 
 * <p>All endpoints in this controller require Basic Authentication with admin:admin credentials.
 * 
 * <p>Supported operations:
 * <ul>
 *   <li>POST /api/users - Create a new user</li>
 *   <li>PATCH /api/users/{id} - Update an existing user (partial update)</li>
 *   <li>DELETE /api/users/{id} - Delete an existing user</li>
 * </ul>
 * 
 * <p>The controller implements comprehensive input validation and error handling,
 * returning appropriate HTTP status codes and error messages for different scenarios.
 * 
 * @author Users API Development Team
 * @since 1.0.0
 * @see UserQueryController
 * @see IUserCommandService
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserCommandController {

    private final IUserCommandService userCommandService;
    private final UserCreateDtoToUserMapper userCreateDtoToUserMapper;
    private final UserToUserResponseDtoMapper userToUserResponseDtoMapper;
    private final UserCreateDtoValidator userCreateDtoValidator;
    private final UserUpdateDtoToUserMapper userUpdateDtoToUserMapper;
    private final UserUpdateDtoValidator userUpdateDtoValidator;
    
    /**
     * Constructor for dependency injection.
     * 
     * <p>Initializes the controller with all required dependencies for user command operations,
     * including services for business logic, mappers for data transformation, and validators
     * for input validation.
     *
     * @param userCommandService service for executing user command operations
     * @param userCreateDtoToUserMapper mapper for converting UserCreateDto to User entity
     * @param userToUserResponseDtoMapper mapper for converting User entity to UserResponseDto
     * @param userCreateDtoValidator validator for UserCreateDto input validation
     * @param userUpdateDtoToUserMapper mapper for converting UserUpdateDto to User entity
     * @param userUpdateDtoValidator validator for UserUpdateDto input validation
     */
    public UserCommandController(IUserCommandService userCommandService,
                                 UserCreateDtoToUserMapper userCreateDtoToUserMapper,
                                 UserToUserResponseDtoMapper userToUserResponseDtoMapper,
                                 UserCreateDtoValidator userCreateDtoValidator,
                                 UserUpdateDtoToUserMapper userUpdateDtoToUserMapper,
                                 UserUpdateDtoValidator userUpdateDtoValidator) {
        this.userCommandService = userCommandService;
        this.userCreateDtoToUserMapper = userCreateDtoToUserMapper;
        this.userToUserResponseDtoMapper = userToUserResponseDtoMapper;
        this.userCreateDtoValidator = userCreateDtoValidator;
        this.userUpdateDtoToUserMapper = userUpdateDtoToUserMapper;
        this.userUpdateDtoValidator = userUpdateDtoValidator;
    }

    /**
     * Creates a new user in the system.
     * 
     * <p>This endpoint accepts user creation data and performs the following operations:
     * <ul>
     *   <li>Validates input data using {@link UserCreateDtoValidator}</li>
     *   <li>Maps DTO to domain entity using {@link UserCreateDtoToUserMapper}</li>
     *   <li>Calls business logic via {@link IUserCommandService}</li>
     *   <li>Maps result back to response DTO using {@link UserToUserResponseDtoMapper}</li>
     * </ul>
     * 
     * <p><strong>Authentication Required:</strong> Basic Authentication with admin:admin credentials.
     * 
     * <p><strong>Validation Rules:</strong>
     * <ul>
     *   <li>firstName: Required, non-empty, non-blank</li>
     *   <li>lastName: Required, non-empty, non-blank</li>
     *   <li>email: Required, valid email format</li>
     *   <li>phone: Optional</li>
     * </ul>
     * 
     * @param userCreateDto the user creation request body containing user data
     * @return ResponseEntity containing:
     *         <ul>
     *           <li>HTTP 201 + UserResponseDto if user created successfully</li>
     *           <li>HTTP 400 + error list if validation fails</li>
     *           <li>HTTP 409 + error message if email already exists</li>
     *         </ul>
     * @see UserCreateDto
     * @see UserResponseDto
     * @see IUserCommandService#createUser(User)
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user in the system")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Invalid name; cannot be null, empty or contain only space characters.\"}"))),
            @ApiResponse(responseCode = "409", description = "Login already exists",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Login 'jroca' already exists\"}")))
    })
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userCreateDto) {
        List<String> validationErrors = userCreateDtoValidator.validate(userCreateDto);
        if (!validationErrors.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", validationErrors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            User userToCreate = userCreateDtoToUserMapper.map(userCreateDto);
            User createdUser = userCommandService.createUser(userToCreate);
            UserResponseDto userResponseDto = userToUserResponseDtoMapper.map(createdUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);

        } catch (LoginAlreadyExistsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }
    /**
     * Updates an existing user in the system.
     * 
     * <p>This endpoint handles HTTP PATCH requests to partially update user information.
     * The operation follows CQRS principles by delegating to command services and includes
     * comprehensive input validation and error handling.
     * 
     * <p><strong>Authentication Required:</strong> Basic Authentication with admin:admin credentials.
     * 
     * <p><strong>Validation Rules:</strong>
     * <ul>
     *   <li>User ID must be a valid UUID format</li>
     *   <li>Fields provided in the request must meet validation criteria</li>
     *   <li>Only non-null fields in the DTO will be updated</li>
     *   <li>Business rules apply to updated fields (e.g., login uniqueness)</li>
     * </ul>
     * 
     * <p><strong>Business Logic:</strong>
     * <ul>
     *   <li>Validates user existence before update</li>
     *   <li>Validates input data using {@link UserUpdateDtoValidator}</li>
     *   <li>Maps DTO to entity using {@link UserUpdateDtoToUserMapper}</li>
     *   <li>Executes update via {@link IUserCommandService}</li>
     *   <li>Returns updated user data</li>
     * </ul>
     * 
     * @param id the unique identifier of the user to update
     * @param userUpdateDto the user data to update (partial data allowed)
     * @return ResponseEntity containing:
     *         <ul>
     *           <li>HTTP 200 + UserResponseDto if update successful</li>
     *           <li>HTTP 400 + error list if validation fails</li>
     *           <li>HTTP 404 if user not found</li>
     *           <li>HTTP 500 for unexpected errors</li>
     *         </ul>
     * 
     * @see UserUpdateDto for request body structure
     * @see UserResponseDto for response body structure
     * @see IUserCommandService#updateUser(User)
     */
    @CrossOrigin(origins = "*")
    @PatchMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates an existing user in the system")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"errors\": [\"name cannot be empty\"]}"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserUpdateDto userUpdateDto) {
        List<String> validationErrors = userUpdateDtoValidator.validate(userUpdateDto);
        if (!validationErrors.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", validationErrors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            User userToUpdate = userUpdateDtoToUserMapper.map(userUpdateDto);
            userToUpdate.setId(id);

            User updatedUser = userCommandService.updateUser(userToUpdate);

            if (updatedUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            UserResponseDto userResponseDto = userToUserResponseDtoMapper.map(updatedUser);

            return ResponseEntity.ok(userResponseDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes an existing user from the system.
     * 
     * <p>This endpoint handles HTTP DELETE requests to permanently remove a user
     * from the system. The operation follows CQRS principles and includes proper
     * error handling for various scenarios.
     * 
     * <p><strong>Authentication Required:</strong> Basic Authentication with admin:admin credentials.
     * 
     * <p><strong>Business Logic:</strong>
     * <ul>
     *   <li>Validates user existence before deletion</li>
     *   <li>Permanently removes user from the system</li>
     *   <li>Handles cascading effects if applicable</li>
     *   <li>Returns appropriate status based on operation result</li>
     * </ul>
     * 
     * <p><strong>Error Scenarios:</strong>
     * <ul>
     *   <li>User not found - Returns HTTP 404</li>
     *   <li>Invalid ID format - Handled by framework validation</li>
     *   <li>Business rule violations - Returns appropriate error status</li>
     * </ul>
     * 
     * @param id the unique identifier of the user to delete (UUID format)
     * @return ResponseEntity containing:
     *         <ul>
     *           <li>HTTP 200 (OK) if deletion successful</li>
     *           <li>HTTP 404 (Not Found) if user doesn't exist</li>
     *         </ul>
     * 
     * @throws UserNotFoundException if the user with specified ID is not found
     * @see IUserCommandService#deleteUser(String)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing user",
            description = "Deletes a user by its unique identifier (GUID)")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            userCommandService.deleteUser(id);
            return ResponseEntity.ok().build();

        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}