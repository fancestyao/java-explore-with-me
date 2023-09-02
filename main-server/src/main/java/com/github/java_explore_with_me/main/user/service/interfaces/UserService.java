package com.github.java_explore_with_me.main.user.service.interfaces;

import com.github.java_explore_with_me.main.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto outputUserDto);

    void deleteUser(Long userId);

    List<UserDto> getUsersInfo(List<Long> ids, int from, int size);
}
