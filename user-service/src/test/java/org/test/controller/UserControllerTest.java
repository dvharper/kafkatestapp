package org.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.test.dto.UserDto;
import org.test.model.User;
import org.test.service.UserService;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_ReturnsList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(
                new User("Alice","alice@test.com",25),
                new User("Bob","bob@test.com",30)
        ));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void createUser_ReturnsCreatedUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Charlie");
        userDto.setEmail("charlie@test.com");
        userDto.setAge(28);

        User created = new User("Charlie", "charlie@test.com", 28);
        when(userService.createUser(anyString(), anyString(), anyInt())).thenReturn(created);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Charlie"));

        verify(userService, times(1)).createUser("Charlie","charlie@test.com",28);
    }
}
