package com.testTasks.testAssignment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.testTasks.testAssignment.UserUtils.getResponseEntity;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    public ResponseEntity<UserDto> findUserById(Long id) {
        UserEntity user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserDto response = mapper.entityToDto(user);
        return getResponseEntity(response);

    }

    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserEntity> userEntityList = (List<UserEntity>) repository.findAll();
        List<UserDto> response = mapper.listEntityToListDto(userEntityList);
        return getResponseEntity(response);
    }

    public ResponseEntity<UserEntity> saveUser(UserEntity newUser) {
        UserEntity response = repository.save(newUser);
        return getResponseEntity(response);
    }

    public ResponseEntity<UserEntity> updateUserById(Long id, UserEntity updateUser) {
        UserEntity response = repository.save(updateUser);
        return getResponseEntity(response);
    }


    public ResponseEntity<UserEntity> updateFields(UserEntity newUser) {
        UserEntity response = repository.save(newUser);
        return getResponseEntity(response);
    }

    public ResponseEntity<Void> deleteUserById(Long id) {
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
