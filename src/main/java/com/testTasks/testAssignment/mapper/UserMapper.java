package com.testTasks.testAssignment.mapper;

import com.testTasks.testAssignment.model.User;
import com.testTasks.testAssignment.model.UserRequestDto;
import com.testTasks.testAssignment.model.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    UserResponseDto entityToResponseDto(User user);

    User responseDtoToEntity(UserResponseDto userResponseDto);

    List<UserResponseDto> listEntityToListDto(List<User> userList);

    List<User> listDtoToListEntity(List<UserResponseDto> userResponseDtoList);

    UserRequestDto entityToRequestDto(User user);

    User requestDtoToEntity(UserRequestDto userResponseDto);
}
