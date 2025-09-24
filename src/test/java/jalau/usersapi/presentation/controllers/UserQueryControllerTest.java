// UserQueryControllerIntegrationTest.java
package jalau.usersapi.presentation.controllers;

import jalau.usersapi.core.domain.entities.User;
import jalau.usersapi.core.domain.services.IUserQueryService;
import jalau.usersapi.presentation.dtos.UserResponseDto;
import jalau.usersapi.presentation.mappers.UserToUserResponseDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = { UserQueryController.class })
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
public class UserQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IUserQueryService userQueryService;

    @MockitoBean
    private UserToUserResponseDtoMapper userToUserResponseDtoMapper;

    @Test
    void getAllUsers_ReturnsListOfUsers() throws Exception {
        // Arrange
        User user1 = new User("aab5d5fd-70c1-11e5-a4fb-b026b977eb28", "João Silva", "joao", "password123");
        User user2 = new User("bcd6e6fe-81d2-22f6-b5fb-c137c088eb29", "Maria Santos", "maria", "password456");

        UserResponseDto dto1 = new UserResponseDto(user1.getId(), user1.getName(), user1.getLogin());
        UserResponseDto dto2 = new UserResponseDto(user2.getId(), user2.getName(), user2.getLogin());

        List<User> users = Arrays.asList(user1, user2);

        when(userQueryService.getUsers()).thenReturn(users);
        when(userToUserResponseDtoMapper.map(user1)).thenReturn(dto1);
        when(userToUserResponseDtoMapper.map(user2)).thenReturn(dto2);

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].name").value(user1.getName()))
                .andExpect(jsonPath("$[0].login").value(user1.getLogin()))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].name").value(user2.getName()))
                .andExpect(jsonPath("$[1].login").value(user2.getLogin()));
    }

    @Test
    void getAllUsers_ReturnsEmptyArrayWhenNoUsers() throws Exception {
        // Arrange
        when(userQueryService.getUsers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(0));
    }
}