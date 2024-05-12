package com.testTasks.testAssignment.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.testTasks.testAssignment.config.ApplicationProperties;
import com.testTasks.testAssignment.model.UserRequestDto;
import com.testTasks.testAssignment.model.UserResponseDto;
import com.testTasks.testAssignment.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ApplicationProperties applicationProperties;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Test
    public void testGetUserById() throws Exception {
        UserResponseDto mockUser = new UserResponseDto();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        given(userService.findUserById(1L)).willReturn(ResponseEntity.ok(mockUser));

        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"));
    }

    @Test
    void getAllUsers() {
    }

    @Test
    public void testCreateNewUser() throws Exception {
        ApplicationProperties.AppConfig mockAppConfig = mock(ApplicationProperties.AppConfig.class);
        when(mockAppConfig.getAge()).thenReturn(18);

        when(applicationProperties.appConfig()).thenReturn(mockAppConfig);

        UserRequestDto newUser = new UserRequestDto();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("carol.jones@example.com");
        newUser.setBirthday(LocalDate.parse("1992-12-24"));

        UserResponseDto savedUser = new UserResponseDto();
        savedUser.setId(1L);
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setEmail("carol.jones@example.com");
        savedUser.setBirthday(LocalDate.parse("1992-12-24"));

        given(userService.saveUser(newUser)).willReturn(ResponseEntity.ok(savedUser));

        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"))
                .andExpect(jsonPath("$.email").value("carol.jones@example.com"))
                .andExpect(jsonPath("$.birthday").value("1992-12-24"));
    }

    @Test
    void updateUser() {
    }

    @Test
    void updateFields() {
    }

    @Test
    void removeUser() {
    }
}