package jalau.usersapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.services.IUserCommandService;
import jalau.usersapi.presentation.dtos.UserCreateDto;
import jalau.usersapi.presentation.dtos.UserResponseDto;
import jalau.usersapi.presentation.mappers.UserCreateDtoToUserMapper;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import jalau.usersapi.presentation.validators.UserCreateDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for UserCommandController
 * 
 * This test class validates the POST /api/users endpoint functionality
 * including request validation and response handling.
 */
@WebMvcTest(UserCommandController.class)
class UserCommandControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private IUserCommandService userCommandService;
    
    @MockBean
    private UserCreateDtoToUserMapper userCreateDtoToUserMapper;
    
    @MockBean
    private UserToUserResponseDtoMapper userToUserResponseDtoMapper;
    
    @MockBean
    private UserCreateDtoValidator userCreateDtoValidator;
    
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
}