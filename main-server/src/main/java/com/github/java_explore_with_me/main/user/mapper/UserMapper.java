package com.github.java_explore_with_me.main.user.mapper;

import com.github.java_explore_with_me.main.user.dto.UserDto;
import com.github.java_explore_with_me.main.user.model.User;
import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDto outputUserDto);

    UserDto userToUserDto(User user);

    List<UserDto> userListToUserDtoList(List<User> all);
}
