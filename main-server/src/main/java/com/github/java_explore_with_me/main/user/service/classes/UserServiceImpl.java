package com.github.java_explore_with_me.main.user.service.classes;

import com.github.java_explore_with_me.main.exception.ConflictException;
import com.github.java_explore_with_me.main.exception.NotFoundException;
import com.github.java_explore_with_me.main.user.dto.UserDto;
import com.github.java_explore_with_me.main.user.mapper.UserMapper;
import com.github.java_explore_with_me.main.user.model.User;
import com.github.java_explore_with_me.main.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

import com.github.java_explore_with_me.main.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto saveUser(UserDto outputUserDto) {
        log.info("Запрос на создание пользователя успешно передан в сервис UserServiceImpl");
        User user = userMapper.userDtoToUser(outputUserDto);
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            log.warn("Произошел конфликт при сохранении пользователя");
            catchSqlException(e);
        }
        log.info("Пользователь: {} успешно сохранен", user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Запрос на удаление пользователя успешно передан в сервис UserServiceImpl");
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователя с id: {} не существует", userId);
            throw new NotFoundException("Пользователь с id: " + userId + " не существует");
        }
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            log.warn("Произошел конфликт при удалении пользователя");
            catchSqlException(e);
        }
        log.info("Пользователь с id: {} успешно удален", userId);
    }

    @Override
    public List<UserDto> getUsersInfo(List<Long> ids, int from, int size) {
        log.info("Запрос на получение списка пользователей успешно передан в сервис UserServiceImpl");
        PageRequest pagination = PageRequest.of(from / size, size);
        List<User> listOfUsers = new ArrayList<>();
        if (ids == null) {
            listOfUsers.addAll(userRepository.findAll(pagination).getContent());
        } else {
            listOfUsers.addAll(userRepository.findAllByIdIn(ids, pagination));
        }
        log.info("Список пользователей: {} успешно получен", listOfUsers);
        return userMapper.userListToUserDtoList(listOfUsers);
    }

    private void catchSqlException(Exception e) {
        StringBuilder stringBuilder = new StringBuilder(e.getCause().getCause().getMessage());
        int indexEqualsSign = stringBuilder.indexOf("=");
        stringBuilder.delete(0, indexEqualsSign + 1);
        throw new ConflictException(stringBuilder.toString().replace("\"", "").trim());
    }
}
