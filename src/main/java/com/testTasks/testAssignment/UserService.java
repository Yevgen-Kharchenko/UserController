package com.testTasks.testAssignment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.testTasks.testAssignment.UserUtils.getResponseEntity;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    public ResponseEntity<UserDto> findUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserDto response = mapper.entityToDto(user);
        return getResponseEntity(response);

    }

    public ResponseEntity<List<UserDto>> findAllUsers(LocalDate from, LocalDate to, Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<User> userEntityList = repository.findAllByParam(from, to, pageable);
        List<UserDto> response = mapper.listEntityToListDto(userEntityList.getContent());
        return getResponseEntity(response);
    }

    public ResponseEntity<User> saveUser(User newUser) {
        User response = repository.save(newUser);
        return getResponseEntity(response);
    }

    public ResponseEntity<User> updateUserById(Long id, User updateUser) {
        User response = repository.save(updateUser);
        return getResponseEntity(response);
    }


    public ResponseEntity<User> updateFields(User newUser) {
        User response = repository.save(newUser);
        return getResponseEntity(response);
    }

    public ResponseEntity<Void> deleteUserById(Long id) {
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
