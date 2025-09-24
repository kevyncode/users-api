package jalau.usersapi.presentation.controllers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.services.IUserCommandService;
import jalau.usersapi.presentation.dtos.UserResponseDto;
import jalau.usersapi.presentation.dtos.UserUpdateDto;
import jalau.usersapi.presentation.mappers.UserCreateDtoToUserMapper;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import jalau.usersapi.presentation.mappers.UserUpdateDtoToUserMapper;
import jalau.usersapi.presentation.validators.UserCreateDtoValidator;
import jalau.usersapi.presentation.validators.UserUpdateDtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandControllerUpdateTest {

    @Mock
    private IUserCommandService userCommandService;

    @Mock
    private UserCreateDtoToUserMapper userCreateDtoToUserMapper;

    @Mock
    private UserToUserResponseDtoMapper userToUserResponseDtoMapper;

    @Mock
    private UserCreateDtoValidator userCreateDtoValidator;

    private UserCommandController userCommandController;

    @Mock
    private UserUpdateDtoToUserMapper userUpdateDtoToUserMapper;

    @Mock
    private UserUpdateDtoValidator userUpdateDtoValidator;

    @BeforeEach
    void setUp() {
        userCommandController = new UserCommandController(
                userCommandService,
                userCreateDtoToUserMapper,
                userToUserResponseDtoMapper,
                userCreateDtoValidator, userUpdateDtoToUserMapper, userUpdateDtoValidator
        );
    }

    @Test
    void updateUser_ValidRequest_Returns200WithUser() {
        UserUpdateDto updateDto = new UserUpdateDto("New Name", "newlogin", "newpassword");
        User userToUpdate = new User(null, "New Name", "newlogin", "newpassword");
        User updatedUser = new User("1", "New Name", "newlogin", "newpassword");
        UserResponseDto responseDto = new UserResponseDto("1", "New Name", "newlogin");

        when(userUpdateDtoValidator.validate(updateDto)).thenReturn(Collections.emptyList());
        when(userUpdateDtoToUserMapper.map(updateDto)).thenReturn(userToUpdate);
        when(userCommandService.updateUser(any(User.class))).thenReturn(updatedUser);
        when(userToUserResponseDtoMapper.map(updatedUser)).thenReturn(responseDto);

        ResponseEntity<?> response = userCommandController.updateUser("1", updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void updateUser_UserNotFound_Returns404() {
        UserUpdateDto updateDto = new UserUpdateDto("New Name", "newlogin", "newpassword");
        User userToUpdate = new User(null, "New Name", "newlogin", "newpassword");

        when(userUpdateDtoValidator.validate(updateDto)).thenReturn(Collections.emptyList());
        when(userUpdateDtoToUserMapper.map(updateDto)).thenReturn(userToUpdate);
        when(userCommandService.updateUser(any(User.class))).thenReturn(null);

        ResponseEntity<?> response = userCommandController.updateUser("999", updateDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateUser_ValidationFails_Returns400() {
        UserUpdateDto updateDto = new UserUpdateDto("", "newlogin", "newpassword");

        when(userUpdateDtoValidator.validate(updateDto))
                .thenReturn(Collections.singletonList("name cannot be empty"));

        ResponseEntity<?> response = userCommandController.updateUser("1", updateDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateUser_PartialUpdate_Returns200() {
        UserUpdateDto updateDto = new UserUpdateDto("New Name", null, null);
        User userToUpdate = new User(null, "New Name", null, null);
        User updatedUser = new User("1", "New Name", "oldlogin", "oldpassword");
        UserResponseDto responseDto = new UserResponseDto("1", "New Name", "oldlogin");

        when(userUpdateDtoValidator.validate(updateDto)).thenReturn(Collections.emptyList());
        when(userUpdateDtoToUserMapper.map(updateDto)).thenReturn(userToUpdate);
        when(userCommandService.updateUser(any(User.class))).thenReturn(updatedUser);
        when(userToUserResponseDtoMapper.map(updatedUser)).thenReturn(responseDto);

        ResponseEntity<?> response = userCommandController.updateUser("1", updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }
}