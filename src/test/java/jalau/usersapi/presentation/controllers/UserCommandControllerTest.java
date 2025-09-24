package jalau.usersapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.services.IUserCommandService;
import jalau.usersapi.core.domain.exceptions.UserNotFoundException;
import jalau.usersapi.presentation.dtos.UserCreateDto;
import jalau.usersapi.presentation.dtos.UserUpdateDto;
import jalau.usersapi.presentation.dtos.UserResponseDto;
import jalau.usersapi.presentation.mappers.UserCreateDtoToUserMapper;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import jalau.usersapi.presentation.validators.UserCreateDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for UserCommandController
 * 
 * This test class validates the POST, PATCH, and DELETE /api/users endpoint functionality
 * including request validation and response handling.
 */
@WebMvcTest(UserCommandController.class)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
class UserCommandControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private IUserCommandService userCommandService;
    
    @MockitoBean
    private UserCreateDtoToUserMapper userCreateDtoToUserMapper;
    
    @MockitoBean
    private UserToUserResponseDtoMapper userToUserResponseDtoMapper;
    
    @MockitoBean
    private UserCreateDtoValidator userCreateDtoValidator;
    
    @MockitoBean
    private jalau.usersapi.presentation.mappers.UserUpdateDtoToUserMapper userUpdateDtoToUserMapper;
    
    @MockitoBean
    private jalau.usersapi.presentation.validators.UserUpdateDtoValidator userUpdateDtoValidator;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void createUser_ValidRequest_Returns201Created() throws Exception {
        // Arrange
        UserCreateDto requestDto = new UserCreateDto("John Doe", "johndoe", "password123");
        User domainUser = new User(null, "John Doe", "johndoe", "password123");
        User createdUser = new User("123e4567-e89b-12d3-a456-426614174000", "John Doe", "johndoe", "password123");
        UserResponseDto responseDto = new UserResponseDto("123e4567-e89b-12d3-a456-426614174000", "John Doe", "johndoe");
        
        when(userCreateDtoValidator.validate(any(UserCreateDto.class))).thenReturn(Arrays.asList());
        when(userCreateDtoToUserMapper.map(any(UserCreateDto.class))).thenReturn(domainUser);
        when(userCommandService.createUser(any(User.class))).thenReturn(createdUser);
        when(userToUserResponseDtoMapper.map(any(User.class))).thenReturn(responseDto);
        
        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.login").value("johndoe"));
        
        verify(userCreateDtoValidator, times(1)).validate(any(UserCreateDto.class));
        verify(userCreateDtoToUserMapper, times(1)).map(any(UserCreateDto.class));
        verify(userCommandService, times(1)).createUser(any(User.class));
        verify(userToUserResponseDtoMapper, times(1)).map(any(User.class));
    }
    
    @Test
    void createUser_InvalidRequest_Returns400BadRequest() throws Exception {
        // Arrange
        UserCreateDto requestDto = new UserCreateDto(null, "johndoe", "password123");
        
        when(userCreateDtoValidator.validate(any(UserCreateDto.class)))
                .thenReturn(Arrays.asList("name is required"));
        
        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("name is required"));
        
        verify(userCreateDtoValidator, times(1)).validate(any(UserCreateDto.class));
        verify(userCreateDtoToUserMapper, never()).map(any(UserCreateDto.class));
        verify(userCommandService, never()).createUser(any(User.class));
        verify(userToUserResponseDtoMapper, never()).map(any(User.class));
    }
    
    @Test
    void createUser_MultipleValidationErrors_Returns400WithAllErrors() throws Exception {
        // Arrange
        UserCreateDto requestDto = new UserCreateDto(null, null, null);
        
        when(userCreateDtoValidator.validate(any(UserCreateDto.class)))
                .thenReturn(Arrays.asList("name is required", "login is required", "password is required"));
        
        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(hasSize(3)));
        
        verify(userCreateDtoValidator, times(1)).validate(any(UserCreateDto.class));
    }
    
    // PATCH /api/users/{id} Tests
    
    @Test
    void updateUser_ValidRequest_Returns200Ok() throws Exception {
        // Arrange
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        UserUpdateDto requestDto = new UserUpdateDto("John Doe Updated", null, null);
        User mappedUser = new User(null, "John Doe Updated", null, null); // Mapper doesn't set ID
        User updatedUser = new User(userId, "John Doe Updated", "johndoe", "password123");
        UserResponseDto responseDto = new UserResponseDto(userId, "John Doe Updated", "johndoe");
        
        when(userUpdateDtoValidator.validate(any(UserUpdateDto.class))).thenReturn(Arrays.asList());
        when(userUpdateDtoToUserMapper.map(any(UserUpdateDto.class))).thenReturn(mappedUser);
        when(userCommandService.updateUser(any(User.class))).thenReturn(updatedUser);
        when(userToUserResponseDtoMapper.map(any(User.class))).thenReturn(responseDto);
        
        // Act & Assert
        mockMvc.perform(patch("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe Updated"))
                .andExpect(jsonPath("$.login").value("johndoe"));
        
        verify(userUpdateDtoValidator, times(1)).validate(any(UserUpdateDto.class));
        verify(userUpdateDtoToUserMapper, times(1)).map(any(UserUpdateDto.class));
        verify(userCommandService, times(1)).updateUser(argThat(user -> 
            userId.equals(user.getId()) && "John Doe Updated".equals(user.getName())));
        verify(userToUserResponseDtoMapper, times(1)).map(any(User.class));
    }
    
    @Test
    void updateUser_InvalidRequest_Returns400BadRequest() throws Exception {
        // Arrange
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        UserUpdateDto requestDto = new UserUpdateDto("", null, null);
        
        when(userUpdateDtoValidator.validate(any(UserUpdateDto.class)))
                .thenReturn(Arrays.asList("name cannot be empty"));
        
        // Act & Assert
        mockMvc.perform(patch("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("name cannot be empty"));
        
        verify(userUpdateDtoValidator, times(1)).validate(any(UserUpdateDto.class));
        verify(userUpdateDtoToUserMapper, never()).map(any(UserUpdateDto.class));
        verify(userCommandService, never()).updateUser(any(User.class));
        verify(userToUserResponseDtoMapper, never()).map(any(User.class));
    }
    
    @Test
    void updateUser_UserNotFound_Returns404NotFound() throws Exception {
        // Arrange
        String userId = "nonexistent-id";
        UserUpdateDto requestDto = new UserUpdateDto("John Doe Updated", null, null);
        User mappedUser = new User(null, "John Doe Updated", null, null);
        
        when(userUpdateDtoValidator.validate(any(UserUpdateDto.class))).thenReturn(Arrays.asList());
        when(userUpdateDtoToUserMapper.map(any(UserUpdateDto.class))).thenReturn(mappedUser);
        when(userCommandService.updateUser(any(User.class))).thenReturn(null); // User not found
        
        // Act & Assert
        mockMvc.perform(patch("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
        
        verify(userUpdateDtoValidator, times(1)).validate(any(UserUpdateDto.class));
        verify(userUpdateDtoToUserMapper, times(1)).map(any(UserUpdateDto.class));
        verify(userCommandService, times(1)).updateUser(any(User.class));
        verify(userToUserResponseDtoMapper, never()).map(any(User.class));
    }
    
    @Test
    void updateUser_MultipleValidationErrors_Returns400WithAllErrors() throws Exception {
        // Arrange
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        UserUpdateDto requestDto = new UserUpdateDto("", "", "");
        
        when(userUpdateDtoValidator.validate(any(UserUpdateDto.class)))
                .thenReturn(Arrays.asList("name cannot be empty", "login cannot be empty", "password cannot be empty"));
        
        // Act & Assert
        mockMvc.perform(patch("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(hasSize(3)));
        
        verify(userUpdateDtoValidator, times(1)).validate(any(UserUpdateDto.class));
    }
    
    // DELETE /api/users/{id} Tests
    
    @Test
    void deleteUser_UserExists_Returns200Ok() throws Exception {
        // Arrange
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        
        // Mock service to not throw exception (user exists)
        doNothing().when(userCommandService).deleteUser(userId);
        
        // Act & Assert
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        
        // Verify service was called
        verify(userCommandService, times(1)).deleteUser(userId);
    }
    
    @Test
    void deleteUser_UserNotFound_Returns404NotFound() throws Exception {
        // Arrange
        String userId = "non-existent-id";
        
        // Mock service to throw UserNotFoundException
        doThrow(new UserNotFoundException(userId)).when(userCommandService).deleteUser(userId);
        
        // Act & Assert
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        
        // Verify service was called
        verify(userCommandService, times(1)).deleteUser(userId);
    }
    
    @Test
    void deleteUser_NullId_Returns404NotFound() throws Exception {
        // Arrange
        String userId = "null-id";
        
        // Mock service to throw UserNotFoundException for null ID
        doThrow(new UserNotFoundException("null")).when(userCommandService).deleteUser(userId);
        
        // Act & Assert
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        
        // Verify service was called
        verify(userCommandService, times(1)).deleteUser(userId);
    }
}