package com.testTasks.testAssignment.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.testTasks.testAssignment.config.ApplicationProperties;
import com.testTasks.testAssignment.exception.DataNotFoundException;
import com.testTasks.testAssignment.model.UserRequestDto;
import com.testTasks.testAssignment.model.UserResponseDto;
import com.testTasks.testAssignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private UserResponseDto updatedUser;
    private UserResponseDto validUser;

    @BeforeEach
    void setUp() {
        validUser = new UserResponseDto();
        validUser.setId(1L);
        validUser.setFirstName("John");
        validUser.setLastName("Doe");
        validUser.setEmail("carol.jones@example.com");
        validUser.setBirthday(LocalDate.parse("1992-12-24"));

        updatedUser = new UserResponseDto();
        updatedUser.setId(1L);
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Doe");
        updatedUser.setEmail("john.doe@example.com");
        updatedUser.setBirthday(LocalDate.of(1990, 1, 1));

        ApplicationProperties.AppConfig mockAppConfig = new ApplicationProperties.AppConfig();
        mockAppConfig.setAge(18);
        mockAppConfig.setMinBirthday("1900-01-01");
        when(applicationProperties.appConfig()).thenReturn(mockAppConfig);
    }

    @Test
    public void testGetUserById() throws Exception {

        given(userService.findUserById(1L)).willReturn(ResponseEntity.ok(validUser));

        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"));
    }

    @Test
    public void testGetAllUsersWithValidParams() throws Exception {

        ResponseEntity<List<UserResponseDto>> users = new ResponseEntity<>((List.of(validUser)), HttpStatus.OK);
        given(userService.findAllUsers(any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt()))
                .willReturn(users);

        mockMvc.perform(get("/api/users/")
                        .param("from", "1990-01-01")
                        .param("to", "2020-01-01")
                        .param("limit", "3")
                        .param("offset", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))); // Assuming the response contains 1 user
    }

    @Test
    public void testGetAllUsersWithInvalidDateRange() throws Exception {

        mockMvc.perform(get("/api/users/")
                        .param("from", "2020-01-01")
                        .param("to", "1990-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateNewUser() throws Exception {

        UserRequestDto newUser = new UserRequestDto();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("carol.jones@example.com");
        newUser.setBirthday(LocalDate.parse("1992-12-24"));

        given(userService.saveUser(newUser)).willReturn(ResponseEntity.ok(validUser));

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
    void updateUser_whenValidRequest_shouldReturnUpdatedUser() throws Exception {
        given(userService.updateUserById(any(Long.class), any(UserRequestDto.class)))
                .willReturn(ResponseEntity.ok(updatedUser));

        mockMvc.perform(put("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.birthday").value("1990-01-01"));
    }

    @Test
    void updateUser_whenInvalidEmail_shouldReturnBadRequest() throws Exception {
        validUser.setEmail("invalid-email");

        mockMvc.perform(put("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_whenInvalidBirthday_shouldReturnBadRequest() throws Exception {
        validUser.setBirthday(LocalDate.now().plusDays(1)); // Future date, invalid

        mockMvc.perform(put("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFields_whenValidUpdate_shouldReturnUpdatedUser() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "john.doe@example.com");
        updates.put("birthday", "1985-12-15");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setEmail("john.doe@example.com");
        responseDto.setBirthday(LocalDate.parse("1985-12-15"));

        ResponseEntity<UserResponseDto> updatedUser = new ResponseEntity<>(responseDto, HttpStatus.OK);

        given(userService.updateFields(any(Long.class), any(Map.class))).willReturn(updatedUser);

        mockMvc.perform(patch("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.birthday").value("1985-12-15"));
    }

    @Test
    void updateFields_whenInvalidEmail_shouldReturnBadRequest() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "invalid-email");

        mockMvc.perform(patch("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFields_whenInvalidBirthdayFormat_shouldReturnBadRequest() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("birthday", "12-31-1990");  // Incorrect format

        mockMvc.perform(patch("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFields_whenBirthdayNotValidAge_shouldReturnBadRequest() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("birthday", LocalDate.now().minusYears(10).toString());  // User too young

        mockMvc.perform(patch("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeUser_whenUserExists_shouldReturnOk() throws Exception {
        Long userId = 1L;
        when(userService.deleteUserById(any(Long.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk());
    }

    @Test
    public void removeUser_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        Long userId = 1L;
        doThrow(new DataNotFoundException("User not found")).when(userService).deleteUserById(userId);

        String actualResponse = mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        JsonNode actualJson = objectMapper.readTree(actualResponse);
        JsonNode expectedJson = objectMapper.readTree("{\"errors\":[{\"status\":404,\"detail\":\"User not found\"," +
                "\"code\":356,\"links\":{\"about\":\"uri=/api/users/1\"}}]}");

        assertEquals(expectedJson, actualJson);
    }
}
