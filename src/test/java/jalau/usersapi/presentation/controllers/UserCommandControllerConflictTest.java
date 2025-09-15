package jalau.usersapi.presentation.controllers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.exceptions.LoginAlreadyExistsException;
import jalau.usersapi.core.domain.services.IUserCommandService;
import jalau.usersapi.presentation.dtos.UserCreateDto;
import jalau.usersapi.presentation.dtos.UserResponseDto;
import jalau.usersapi.presentation.mappers.UserCreateDtoToUserMapper;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import jalau.usersapi.presentation.validators.UserCreateDtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserCommandController login conflict scenarios
 */
@ExtendWith(MockitoExtension.class)
public class UserCommandControllerConflictTest {

    @Mock
    private IUserCommandService userCommandService;
    
    @Mock
    private UserCreateDtoToUserMapper userCreateDtoToUserMapper;
    
    @Mock
    private UserToUserResponseDtoMapper userToUserResponseDtoMapper;
    
    @Mock
    private UserCreateDtoValidator userCreateDtoValidator;

    private UserCommandController userCommandController;

    @BeforeEach
    void setUp() {
        userCommandController = new UserCommandController(
            userCommandService,
            userCreateDtoToUserMapper,
            userToUserResponseDtoMapper,
            userCreateDtoValidator
        );
    }

    @Test
    void createUser_WhenLoginAlreadyExists_ShouldReturn409Conflict() {
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("João Silva");
        userCreateDto.setLogin("joao.silva");
        userCreateDto.setPassword("senha123");

        User userToCreate = new User(null, "João Silva", "joao.silva", "senha123");
        
        when(userCreateDtoValidator.validate(userCreateDto)).thenReturn(Collections.emptyList());
        when(userCreateDtoToUserMapper.map(userCreateDto)).thenReturn(userToCreate);
        when(userCommandService.createUser(userToCreate))
            .thenThrow(new LoginAlreadyExistsException("joao.silva"));

        // Act
        ResponseEntity<?> response = userCommandController.createUser(userCreateDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("User with login 'joao.silva' already exists", responseBody.get("error"));
        
        verify(userCreateDtoValidator, times(1)).validate(userCreateDto);
        verify(userCreateDtoToUserMapper, times(1)).map(userCreateDto);
        verify(userCommandService, times(1)).createUser(userToCreate);
        verify(userToUserResponseDtoMapper, never()).map(any());
    }

    @Test
    void createUser_WhenLoginDoesNotExist_ShouldReturn201Created() {
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("João Silva");
        userCreateDto.setLogin("joao.silva");
        userCreateDto.setPassword("senha123");

        User userToCreate = new User(null, "João Silva", "joao.silva", "senha123");
        User createdUser = new User("123e4567-e89b-12d3-a456-426614174000", "João Silva", "joao.silva", "senha123");
        
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId("123e4567-e89b-12d3-a456-426614174000");
        userResponseDto.setName("João Silva");
        userResponseDto.setLogin("joao.silva");
        
        when(userCreateDtoValidator.validate(userCreateDto)).thenReturn(Collections.emptyList());
        when(userCreateDtoToUserMapper.map(userCreateDto)).thenReturn(userToCreate);
        when(userCommandService.createUser(userToCreate)).thenReturn(createdUser);
        when(userToUserResponseDtoMapper.map(createdUser)).thenReturn(userResponseDto);

        // Act
        ResponseEntity<?> response = userCommandController.createUser(userCreateDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        UserResponseDto responseBody = (UserResponseDto) response.getBody();
        assertNotNull(responseBody);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", responseBody.getId());
        assertEquals("João Silva", responseBody.getName());
        assertEquals("joao.silva", responseBody.getLogin());
        
        verify(userCreateDtoValidator, times(1)).validate(userCreateDto);
        verify(userCreateDtoToUserMapper, times(1)).map(userCreateDto);
        verify(userCommandService, times(1)).createUser(userToCreate);
        verify(userToUserResponseDtoMapper, times(1)).map(createdUser);
    }

    @Test
    void createUser_WhenValidationFailsAndLoginExists_ShouldReturn400BadRequest() {
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("");
        userCreateDto.setLogin("joao.silva");
        userCreateDto.setPassword("senha123");

        when(userCreateDtoValidator.validate(userCreateDto))
            .thenReturn(Collections.singletonList("Name is required and cannot be empty"));

        // Act
        ResponseEntity<?> response = userCommandController.createUser(userCreateDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        // Should not check for login duplication if validation fails
        verify(userCreateDtoValidator, times(1)).validate(userCreateDto);
        verify(userCreateDtoToUserMapper, never()).map(any());
        verify(userCommandService, never()).createUser(any());
        verify(userToUserResponseDtoMapper, never()).map(any());
    }
}