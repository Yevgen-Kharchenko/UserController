package com.testTasks.testAssignment;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-12T13:22:01+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.4.1 (JetBrains s.r.o.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto entityToDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        if ( user.getId() != null ) {
            userDto.setId( user.getId() );
        }
        if ( user.getFirstName() != null ) {
            userDto.setFirstName( user.getFirstName() );
        }
        if ( user.getLastName() != null ) {
            userDto.setLastName( user.getLastName() );
        }
        if ( user.getEmail() != null ) {
            userDto.setEmail( user.getEmail() );
        }
        if ( user.getBirthday() != null ) {
            userDto.setBirthday( user.getBirthday() );
        }
        if ( user.getAddress() != null ) {
            userDto.setAddress( user.getAddress() );
        }
        if ( user.getPhone() != null ) {
            userDto.setPhone( user.getPhone() );
        }

        return userDto;
    }

    @Override
    public User dtoToEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        if ( userDto.getId() != null ) {
            user.setId( userDto.getId() );
        }
        if ( userDto.getFirstName() != null ) {
            user.setFirstName( userDto.getFirstName() );
        }
        if ( userDto.getLastName() != null ) {
            user.setLastName( userDto.getLastName() );
        }
        if ( userDto.getEmail() != null ) {
            user.setEmail( userDto.getEmail() );
        }
        if ( userDto.getBirthday() != null ) {
            user.setBirthday( userDto.getBirthday() );
        }
        if ( userDto.getAddress() != null ) {
            user.setAddress( userDto.getAddress() );
        }
        if ( userDto.getPhone() != null ) {
            user.setPhone( userDto.getPhone() );
        }

        return user;
    }

    @Override
    public List<UserDto> listEntityToListDto(List<User> userList) {
        if ( userList == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( userList.size() );
        for ( User user : userList ) {
            list.add( entityToDto( user ) );
        }

        return list;
    }

    @Override
    public List<User> listDtoToListEntity(List<UserDto> userDtoList) {
        if ( userDtoList == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( userDtoList.size() );
        for ( UserDto userDto : userDtoList ) {
            list.add( dtoToEntity( userDto ) );
        }

        return list;
    }
}
