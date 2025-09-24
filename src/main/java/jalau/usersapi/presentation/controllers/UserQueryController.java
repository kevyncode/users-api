package jalau.usersapi.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.services.IUserQueryService;
import jalau.usersapi.presentation.dtos.UserResponseDto;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for User query operations following CQRS pattern.
 * 
 * <p>This controller handles all HTTP GET requests related to users, implementing
 * the Query side of the Command Query Responsibility Segregation (CQRS) pattern.
 * It provides endpoints for retrieving user data without modifying state.
 * 
 * <p>All endpoints in this controller require Basic Authentication with admin:admin credentials.
 * 
 * <p>Supported operations:
 * <ul>
 *   <li>GET /api/users - Retrieve all users</li>
 *   <li>GET /api/users/{id} - Retrieve a specific user by ID</li>
 * </ul>
 * 
 * @author Users API Development Team
 * @since 1.0.0
 * @see UserCommandController
 * @see IUserQueryService
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserQueryController {

    private final IUserQueryService userQueryService;
    private final UserToUserResponseDtoMapper userToUserResponseDtoMapper;

    /**
     * Constructor for dependency injection.
     * 
     * @param userQueryService service for user query operations
     * @param userToUserResponseDtoMapper mapper for converting domain entities to response DTOs
     */
    public UserQueryController(IUserQueryService userQueryService,
                               UserToUserResponseDtoMapper userToUserResponseDtoMapper) {
        this.userQueryService = userQueryService;
        this.userToUserResponseDtoMapper = userToUserResponseDtoMapper;
    }

    /**
     * Retrieves all users from the system.
     * 
     * <p>This endpoint returns a list of all users currently stored in the system.
     * The response will always return HTTP 200 OK, even if no users exist (returns empty array).
     * 
     * <p><strong>Authentication Required:</strong> Basic Authentication with admin:admin credentials.
     * 
     * <p><strong>CORS:</strong> Accepts requests from any origin.
     *
     * @return ResponseEntity containing a list of UserResponseDto objects with HTTP 200 status.
     *         Returns empty list if no users exist in the system.
     * @see UserResponseDto
     */
    @CrossOrigin(origins = "*")
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users from the system")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto[].class)))
    })
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        var users = userQueryService.getUsers();

        List<UserResponseDto> userDtos = users.stream()
                .map(userToUserResponseDtoMapper::map)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    /**
     * Retrieves a specific user by their unique identifier.
     * 
     * <p>This endpoint searches for a user with the provided UUID and returns their information
     * if found. If no user exists with the given ID, returns HTTP 404 Not Found.
     * 
     * <p><strong>Authentication Required:</strong> Basic Authentication with admin:admin credentials.
     * 
     * <p><strong>CORS:</strong> Accepts requests from any origin.
     * 
     * @param id the unique identifier (UUID) of the user to retrieve
     * @return ResponseEntity containing UserResponseDto with HTTP 200 if user is found,
     *         or HTTP 404 if user does not exist
     * @see UserResponseDto
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a single user by their unique identifier")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
        User user = userQueryService.getUser(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponseDto userResponseDto = userToUserResponseDtoMapper.map(user);
        return ResponseEntity.ok(userResponseDto);
    }
}
