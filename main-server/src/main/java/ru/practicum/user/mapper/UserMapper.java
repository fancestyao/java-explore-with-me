package ru.practicum.user.mapper;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDto outputUserDto);

    UserDto userToUserDto(User user);

    List<UserDto> userListToUserDtoList(List<User> all);
}
