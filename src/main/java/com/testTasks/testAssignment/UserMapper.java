package com.testTasks.testAssignment;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    UserDto entityToDto(User user);

    User dtoToEntity(UserDto userDto);

    List<UserDto> listEntityToListDto(List<User> userList);

    List<User> listDtoToListEntity(List<UserDto> userDtoList);
}
