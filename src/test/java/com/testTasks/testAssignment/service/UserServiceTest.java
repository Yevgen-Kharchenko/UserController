package com.testTasks.testAssignment.service;

import com.testTasks.testAssignment.exception.DataNotFoundException;
import com.testTasks.testAssignment.mapper.UserMapper;
import com.testTasks.testAssignment.model.User;
import com.testTasks.testAssignment.model.UserRequestDto;
import com.testTasks.testAssignment.model.UserResponseDto;
import com.testTasks.testAssignment.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    private User existingUser;
    private UserResponseDto updatedUserResponse;
    private UserRequestDto updateUserRequest;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setEmail("existing.user@example.com");
        existingUser.setBirthday(LocalDate.of(1990, 1, 1));

        updateUserRequest = new UserRequestDto();
        updateUserRequest.setFirstName("Updated");
        updateUserRequest.setLastName("User");
        updateUserRequest.setEmail("updated.user@example.com");
        updateUserRequest.setBirthday(LocalDate.of(1990, 1, 1));

        updatedUserResponse = new UserResponseDto();
        updatedUserResponse.setId(1L);
        updatedUserResponse.setFirstName("UpdatedJohn");
        updatedUserResponse.setLastName("User");
        updatedUserResponse.setEmail("updated.user@example.com");
        updatedUserResponse.setBirthday(LocalDate.of(1990, 1, 1));

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(existingUser));
        when(repository.save(any(User.class))).thenReturn(existingUser);
        when(mapper.entityToResponseDto(any(User.class))).thenReturn(updatedUserResponse);
    }

    @Test
    void findUserById_whenUserExists_returnsUser() {
        given(repository.findById(1L)).willReturn(java.util.Optional.of(existingUser));
        given(mapper.entityToResponseDto(any(User.class))).willReturn(updatedUserResponse);

        ResponseEntity<UserResponseDto> response = service.findUserById(1L);

        assertNotNull(response.getBody());
        assertEquals("UpdatedJohn", response.getBody().getFirstName());
    }

    @Test
    void findUserById_whenUserDoesNotExist_throwsException() {
        given(repository.findById(anyLong())).willReturn(java.util.Optional.empty());

        assertThrows(DataNotFoundException.class, () -> service.findUserById(1L));
    }

    @Test
    void findAllUsers_returnsUserList() {
        Page<User> page = new PageImpl<>(Collections.singletonList(existingUser));
        given(repository.findAllByParam(any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).willReturn(page);
        given(mapper.listEntityToListDto(anyList())).willReturn(Collections.singletonList(updatedUserResponse));

        ResponseEntity<List<UserResponseDto>> response = service.findAllUsers(LocalDate.now(), LocalDate.now().plusDays(1), 1, 0);

        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void saveUser_createsUser() {
        given(repository.save(any(User.class))).willReturn(existingUser);
        given(mapper.requestDtoToEntity(any(UserRequestDto.class))).willReturn(existingUser);
        given(mapper.entityToResponseDto(any(User.class))).willReturn(updatedUserResponse);

        ResponseEntity<UserResponseDto> response = service.saveUser(new UserRequestDto());

        assertNotNull(response.getBody());
        assertEquals("UpdatedJohn", response.getBody().getFirstName());
    }

    @Test
    void updateUserById_whenUserExists_updatesAndReturnsUser() {
        given(repository.findById(1L)).willReturn(Optional.of(existingUser));
        given(mapper.requestDtoToEntity(updateUserRequest)).willReturn(existingUser);
        given(mapper.entityToResponseDto(any(User.class))).willReturn(updatedUserResponse);
        given(repository.save(any(User.class))).willReturn(existingUser);

        ResponseEntity<UserResponseDto> response = service.updateUserById(1L, updateUserRequest);

        assertNotNull(response.getBody());
        assertEquals("UpdatedJohn", response.getBody().getFirstName());
        assertEquals("updated.user@example.com", response.getBody().getEmail());
        verify(repository).save(any(User.class));
    }

    @Test
    void updateUserById_whenUserNotFound_throwsException() {
        given(repository.findById(1L)).willReturn(Optional.empty());

        Exception exception = assertThrows(DataNotFoundException.class, () -> {
            service.updateUserById(1L, updateUserRequest);
        });

        assertEquals("User not found with id = 1", exception.getMessage());
    }

    @Test
    void updateFields_whenUserExists_updatesAndReturnsUser() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", "UpdatedJohn");
        updates.put("lastName", "UpdatedDoe");

        ResponseEntity<UserResponseDto> response = service.updateFields(1L, updates);

        assertNotNull(response.getBody());
        assertEquals("UpdatedJohn", response.getBody().getFirstName()); // Assume applyUpdates works correctly
        verify(repository).save(existingUser);
    }

    @Test
    void updateFields_whenUserNotFound_throwsDataNotFoundException() {
        when(repository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(DataNotFoundException.class, () -> {
            service.updateFields(1L, new HashMap<>());
        });

        assertEquals("User not found with id = 1", exception.getMessage());
    }

    @Test
    void deleteUserById_deletesUser() {
        doNothing().when(repository).deleteById(anyLong());

        ResponseEntity<Void> response = service.deleteUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
