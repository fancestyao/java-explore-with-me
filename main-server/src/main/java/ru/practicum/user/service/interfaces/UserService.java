package ru.practicum.user.service.interfaces;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto outputUserDto);

    void deleteUser(Long userId);

    List<UserDto> getUsersInfo(List<Long> ids, int from, int size);
}
