package com.testTasks.testAssignment.service;

import com.testTasks.testAssignment.exception.DataNotFoundException;
import com.testTasks.testAssignment.mapper.UserMapper;
import com.testTasks.testAssignment.model.User;
import com.testTasks.testAssignment.model.UserRequestDto;
import com.testTasks.testAssignment.model.UserResponseDto;
import com.testTasks.testAssignment.repo.UserRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import static com.testTasks.testAssignment.util.UserUtils.getResponseEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    // Fetches a user by ID and returns a DTO response
    public ResponseEntity<UserResponseDto> findUserById(Long id) {
        log.info("Attempting to find user by ID: {}", id);
        User user = repository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found with id = " + id));
        UserResponseDto response = mapper.entityToResponseDto(user);
        log.info("User found and processed: {}", user);
        return getResponseEntity(response);

    }

    // Retrieves all users within the specified date range with pagination
    public ResponseEntity<List<UserResponseDto>> findAllUsers(LocalDate from, LocalDate to, Integer limit, Integer offset) {
        log.info("Fetching all users from {} to {}, limit {}, offset {}", from, to, limit, offset);
        Pageable pageable = PageRequest.of(offset, limit);
        Page<User> userEntityList = repository.findAllByParam(from, to, pageable);
        List<UserResponseDto> response = mapper.listEntityToListDto(userEntityList.getContent());
        log.info("Fetched {} users.", userEntityList.getNumberOfElements());
        return getResponseEntity(response);
    }

    // Saves a new user to the database
    @Transactional
    public ResponseEntity<UserResponseDto> saveUser(UserRequestDto newUser) {
        log.info("Saving new user: {}", newUser);
        User request = mapper.requestDtoToEntity(newUser);
        UserResponseDto response = mapper.entityToResponseDto(repository.save(request));
        log.info("User saved: {}", request);
        return getResponseEntity(response);
    }

    // Updates an existing user by ID
    @Transactional
    public ResponseEntity<UserResponseDto> updateUserById(Long id, UserRequestDto request) {
        log.info("Updating user ID: {}", id);
        User updateUser = mapper.requestDtoToEntity(request);
        User result = repository.findById(id)
                .map(user -> {
                    user.setFirstName(updateUser.getFirstName());
                    user.setLastName(updateUser.getLastName());
                    user.setEmail(updateUser.getEmail());
                    user.setBirthday(updateUser.getBirthday());
                    user.setAddress(updateUser.getAddress());
                    user.setPhone(updateUser.getPhone());
                    log.info("Updated user details for ID: {}", id);
                    return repository.save(user);
                })
                .orElseThrow(() -> new DataNotFoundException("User not found with id = " + id));
        UserResponseDto response = mapper.entityToResponseDto(result);
        return getResponseEntity(response);
    }

    // Applies updates to specific fields of an existing user
    @Transactional
    public ResponseEntity<UserResponseDto> updateFields(Long id, Map<String, Object> updates) {
        log.info("Updating fields for user ID: {}", id);
        User user = repository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found with id = " + id));
        applyUpdates(user, updates);
        UserResponseDto response = mapper.entityToResponseDto(repository.save(user));
        log.info("User fields updated: {}", user);
        return getResponseEntity(response);
    }

    // Deletes a user by ID
    @Transactional
    public ResponseEntity<Void> deleteUserById(Long id) {
        log.info("Deleting user with ID: {}", id);
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getColumnName(Field field) {
        Column annotation = field.getAnnotation(Column.class);
        if (annotation != null && !annotation.name().isEmpty()) {
            return annotation.name();
        }
        return field.getName();
    }

    private void applyUpdates(User user, Map<String, Object> updates) {
        Class<?> clazz = user.getClass();
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                Field field = null;
                for (Field f : clazz.getDeclaredFields()) {
                    if (getColumnName(f).equals(key)) {
                        field = f;
                        break;
                    }
                }
                if (field != null) {
                    field.setAccessible(true);
                    if (field.getType() == LocalDate.class && value instanceof String) {
                        try {
                            LocalDate dateValue = LocalDate.parse((String) value, DateTimeFormatter.ISO_LOCAL_DATE);
                            field.set(user, dateValue);
                        } catch (DateTimeParseException e) {
                            log.error("Invalid date format for field {}: {}", key, value);
                            throw new IllegalArgumentException("Invalid date format for " + key);
                        }
                    } else {
                        field.set(user, value);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("Error accessing field {}: {}", key, e.getMessage());
                throw new RuntimeException("Error updating field: " + key, e);
            }
        }
    }
}
