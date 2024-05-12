package com.testTasks.testAssignment.service;

import com.testTasks.testAssignment.exception.DataNotFoundException;
import com.testTasks.testAssignment.mapper.UserMapper;
import com.testTasks.testAssignment.model.User;
import com.testTasks.testAssignment.model.UserRequestDto;
import com.testTasks.testAssignment.model.UserResponseDto;
import com.testTasks.testAssignment.repo.UserRepository;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public ResponseEntity<UserResponseDto> findUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found with id = " + id));
        UserResponseDto response = mapper.entityToResponseDto(user);
        return getResponseEntity(response);

    }

    public ResponseEntity<List<UserResponseDto>> findAllUsers(LocalDate from, LocalDate to, Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<User> userEntityList = repository.findAllByParam(from, to, pageable);
        List<UserResponseDto> response = mapper.listEntityToListDto(userEntityList.getContent());
        return getResponseEntity(response);
    }

    public ResponseEntity<User> saveUser(UserRequestDto newUser) {
        User request = mapper.requestDtoToEntity(newUser);
        User response = repository.save(request);
        return getResponseEntity(response);
    }

    @Transactional
    public ResponseEntity<User> updateUserById(Long id, UserRequestDto request) {
        User updateUser = mapper.requestDtoToEntity(request);
        User response = repository.findById(id)
                .map(user -> {
                    user.setFirstName(updateUser.getFirstName());
                    user.setLastName(updateUser.getLastName());
                    user.setEmail(updateUser.getEmail());
                    user.setBirthday(updateUser.getBirthday());
                    user.setAddress(updateUser.getAddress());
                    user.setPhone(updateUser.getPhone());
                    return repository.save(user);
                })
                .orElseThrow(() -> new DataNotFoundException("User not found with id = " + id));
        return getResponseEntity(response);
    }


    @Transactional
    public ResponseEntity<User> updateFields(Long id, Map<String, Object> updates) {
        User user = repository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found with id = " + id));
        applyUpdates(user, updates);
        User response = repository.save(user);
        return getResponseEntity(response);
    }

    @Transactional
    public ResponseEntity<Void> deleteUserById(Long id) {
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void applyUpdates(User user, Map<String, Object> updates) {
        Class<?> clazz = user.getClass();
        updates.forEach((key, value) -> {
            try {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                if (field.getType() == LocalDate.class && value instanceof String) {
                    try {
                        LocalDate dateValue = LocalDate.parse((String) value, DateTimeFormatter.ISO_LOCAL_DATE);
                        field.set(user, dateValue);
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException("Invalid date format for " + key);
                    }
                } else {
                    field.set(user, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Error updating field: " + key, e);
            }
        });
    }
}