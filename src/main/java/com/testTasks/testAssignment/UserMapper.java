package com.testTasks.testAssignment;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    UserDto entityToDto(UserEntity userEntity);

    UserEntity dtoToEntity(UserDto userDto);

    List<UserDto> listEntityToListDto(List<UserEntity> userEntityList);

    List<UserEntity>   listDtoToListEntity(List<UserDto> userDtoList);
}
