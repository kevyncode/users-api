package jalau.usersapi.integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.services.IUserCommandService;
import jalau.usersapi.core.domain.services.IUserQueryService;
import jalau.usersapi.presentation.dtos.UserCreateDto;
import jalau.usersapi.presentation.dtos.UserUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Basic Authentication security
 * 
 * Tests all endpoints with different authentication scenarios:
 * - Valid credentials (admin:admin)
 * - Missing Authorization header
 * - Invalid credentials
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class BasicAuthenticationIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IUserQueryService userQueryService;

    @MockitoBean
    private IUserCommandService userCommandService;

    private MockMvc mockMvc;

    // Base64 encoded "admin:admin" = YWRtaW46YWRtaW4=
    private static final String VALID_AUTH_HEADER = "Basic " + 
        Base64.getEncoder().encodeToString("admin:admin".getBytes());
    
    // Base64 encoded "wrong:credentials" = d3JvbmcdcmVkZW50aWFscw==
    private static final String INVALID_AUTH_HEADER = "Basic " + 
        Base64.getEncoder().encodeToString("wrong:credentials".getBytes());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    // =====================================================
    // GET /api/users Tests
    // =====================================================

    @Test
    void getUsersWithValidAuth_Returns200Ok() throws Exception {
        // Arrange
        List<User> users = Arrays.asList(
            new User("1", "John Doe", "johndoe", "password"),
            new User("2", "Jane Smith", "janesmith", "password")
        );
        when(userQueryService.getUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/users")
                .header("Authorization", VALID_AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getUsersWithoutAuth_Returns401Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUsersWithInvalidAuth_Returns401Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users")
                .header("Authorization", INVALID_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    // =====================================================
    // POST /api/users Tests
    // =====================================================

    @Test
    void createUserWithValidAuth_Returns201Created() throws Exception {
        // Arrange
        UserCreateDto requestDto = new UserCreateDto("John Doe", "johndoe", "password123");
        User createdUser = new User("123e4567-e89b-12d3-a456-426614174000", "John Doe", "johndoe", "password123");
        
        when(userCommandService.createUser(any(User.class))).thenReturn(createdUser);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .header("Authorization", VALID_AUTH_HEADER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.login").value("johndoe"));
    }

    @Test
    void createUserWithoutAuth_Returns401Unauthorized() throws Exception {
        // Arrange
        UserCreateDto requestDto = new UserCreateDto("John Doe", "johndoe", "password123");

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createUserWithInvalidAuth_Returns401Unauthorized() throws Exception {
        // Arrange
        UserCreateDto requestDto = new UserCreateDto("John Doe", "johndoe", "password123");

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .header("Authorization", INVALID_AUTH_HEADER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    // =====================================================
    // PATCH /api/users/{id} Tests
    // =====================================================

    @Test
    void updateUserWithValidAuth_Returns200Ok() throws Exception {
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        UserUpdateDto requestDto = new UserUpdateDto("John Doe Updated", null, null);
        User updatedUser = new User(userId, "John Doe Updated", "johndoe", "password123");
        
        when(userCommandService.updateUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/api/users/{id}", userId)
                .header("Authorization", VALID_AUTH_HEADER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe Updated"))
                .andExpect(jsonPath("$.login").value("johndoe"));
    }

    @Test
    void updateUserWithoutAuth_Returns401Unauthorized() throws Exception {
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        UserUpdateDto requestDto = new UserUpdateDto("John Doe Updated", null, null);

        mockMvc.perform(patch("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateUserWithInvalidAuth_Returns401Unauthorized() throws Exception {
        // Arrange
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        UserUpdateDto requestDto = new UserUpdateDto("John Doe Updated", null, null);

        // Act & Assert
        mockMvc.perform(patch("/api/users/{id}", userId)
                .header("Authorization", INVALID_AUTH_HEADER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    // =====================================================
    // DELETE /api/users/{id} Tests
    // =====================================================

    @Test
    void deleteUserWithValidAuth_Returns200Ok() throws Exception {
        // Arrange
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        
        // Mock service to not throw exception (user exists)
        // No need to mock anything as void method

        // Act & Assert
        mockMvc.perform(delete("/api/users/{id}", userId)
                .header("Authorization", VALID_AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void deleteUserWithoutAuth_Returns401Unauthorized() throws Exception {
        // Arrange
        String userId = "123e4567-e89b-12d3-a456-426614174000";

        // Act & Assert
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteUserWithInvalidAuth_Returns401Unauthorized() throws Exception {
        // Arrange
        String userId = "123e4567-e89b-12d3-a456-426614174000";

        // Act & Assert
        mockMvc.perform(delete("/api/users/{id}", userId)
                .header("Authorization", INVALID_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }
}