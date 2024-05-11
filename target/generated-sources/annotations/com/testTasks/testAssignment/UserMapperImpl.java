package com.testTasks.testAssignment;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-12T01:30:41+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.4.1 (JetBrains s.r.o.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto entityToDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        if ( userEntity.getId() != null ) {
            userDto.setId( userEntity.getId() );
        }
        if ( userEntity.getFirstName() != null ) {
            userDto.setFirstName( userEntity.getFirstName() );
        }
        if ( userEntity.getLastName() != null ) {
            userDto.setLastName( userEntity.getLastName() );
        }
        if ( userEntity.getEmail() != null ) {
            userDto.setEmail( userEntity.getEmail() );
        }
        if ( userEntity.getBirthday() != null ) {
            userDto.setBirthday( userEntity.getBirthday() );
        }
        if ( userEntity.getAddress() != null ) {
            userDto.setAddress( userEntity.getAddress() );
        }
        if ( userEntity.getPhone() != null ) {
            userDto.setPhone( userEntity.getPhone() );
        }

        return userDto;
    }

    @Override
    public UserEntity dtoToEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        if ( userDto.getId() != null ) {
            userEntity.setId( userDto.getId() );
        }
        if ( userDto.getFirstName() != null ) {
            userEntity.setFirstName( userDto.getFirstName() );
        }
        if ( userDto.getLastName() != null ) {
            userEntity.setLastName( userDto.getLastName() );
        }
        if ( userDto.getEmail() != null ) {
            userEntity.setEmail( userDto.getEmail() );
        }
        if ( userDto.getBirthday() != null ) {
            userEntity.setBirthday( userDto.getBirthday() );
        }
        if ( userDto.getAddress() != null ) {
            userEntity.setAddress( userDto.getAddress() );
        }
        if ( userDto.getPhone() != null ) {
            userEntity.setPhone( userDto.getPhone() );
        }

        return userEntity;
    }

    @Override
    public List<UserDto> listEntityToListDto(List<UserEntity> userEntityList) {
        if ( userEntityList == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( userEntityList.size() );
        for ( UserEntity userEntity : userEntityList ) {
            list.add( entityToDto( userEntity ) );
        }

        return list;
    }

    @Override
    public List<UserEntity> listDtoToListEntity(List<UserDto> userDtoList) {
        if ( userDtoList == null ) {
            return null;
        }

        List<UserEntity> list = new ArrayList<UserEntity>( userDtoList.size() );
        for ( UserDto userDto : userDtoList ) {
            list.add( dtoToEntity( userDto ) );
        }

        return list;
    }
}
